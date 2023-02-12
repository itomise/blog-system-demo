package com.itomise.admin.lib.google

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isSuccessful
import com.itomise.admin.lib.fuel.responseCustomObject
import com.itomise.admin.module.envConfig
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.SignedJWT
import io.ktor.util.*
import org.apache.http.client.utils.URIBuilder
import java.net.URL
import java.security.SecureRandom
import java.time.Instant
import java.util.*


object GoogleAuthentication {
    private const val GOOGLE_OPENID_CONFIGURATION_URI = "https://accounts.google.com/.well-known/openid-configuration"
    private val objectMapper = jacksonObjectMapper()

    fun createOpenConnectAuthURI(state: String): String {
        val nonce = SecureRandom.getInstance("SHA1PRNG").generateSeed(32)

        val configuration = getGoogleOpenidConfiguration()

        return URIBuilder(configuration.authorizationEndpoint)
            .addParameter("response_type", "code")
            .addParameter("client_id", envConfig.google.oauth2ClientId)
            .addParameter("scope", "openid profile email")
            .addParameter("redirect_uri", envConfig.urls.googleOAuth2CallbackUrl)
            .addParameter("state", state)
            .addParameter("nonce", hex(nonce))
            .build().toString()
    }

    fun getGoogleUserInfoByCode(code: String): GoogleUserInfoInToken {
        val configuration = getGoogleOpenidConfiguration()
        val (_, response, result) = Fuel.post(configuration.tokenEndpoint)
            .jsonBody(
                objectMapper.writeValueAsString(
                    mapOf(
                        "code" to code,
                        "client_id" to envConfig.google.oauth2ClientId,
                        "client_secret" to envConfig.google.oauth2ClientSecret,
                        "redirect_uri" to envConfig.urls.googleOAuth2CallbackUrl,
                        "grant_type" to "authorization_code"
                    )
                )
            ).responseCustomObject<TokenResponse>()

        if (!response.isSuccessful) throw IllegalStateException("token の取得に失敗しました。")

        val tokenResponse = result.get()

        val jwt = SignedJWT.parse(tokenResponse.idToken)
        val jwk = JWKSet.load(URL(configuration.jwksUri)).getKeyByKeyId(jwt.header.keyID).toRSAKey()

        if (!jwt.verify(RSASSAVerifier(jwk))) throw IllegalStateException("token が不正です。")

        val isValidIss = jwt.jwtClaimsSet.getClaim("iss").toString().contains("accounts.google.com")
        val isValidAud = jwt.jwtClaimsSet.getStringArrayClaim("aud")[0] == envConfig.google.oauth2ClientId
        val isValidExp = jwt.jwtClaimsSet.getDateClaim("exp").after(Date.from(Instant.now()))

        if (!isValidIss || !isValidAud || !isValidExp) throw IllegalStateException("token が不正です。")

        return GoogleUserInfoInToken(
            email = jwt.jwtClaimsSet.getClaim("email").toString(),
            emailVerified = jwt.jwtClaimsSet.getClaim("email_verified").toString() == "true",
            familyName = jwt.jwtClaimsSet.getClaim("family_name").toString(),
            givenName = jwt.jwtClaimsSet.getClaim("given_name").toString(),
            name = jwt.jwtClaimsSet.getClaim("name").toString()
        )

    }

    data class GoogleUserInfoInToken(
        val email: String,
        val emailVerified: Boolean,
        val familyName: String,
        val givenName: String,
        val name: String
    )

    data class TokenResponse(
        val accessToken: String,
        val expiresIn: Int,
        val idToken: String,
        val scope: String,
        val tokenType: String,
        val refreshToken: String?
    )

    private fun getGoogleOpenidConfiguration(): OpenidConfiguration {
        val (_, response, result) = Fuel.get(GOOGLE_OPENID_CONFIGURATION_URI)
            .responseCustomObject<OpenidConfiguration>()
        if (!response.isSuccessful) throw IllegalStateException("Google Discovery Document の取得に失敗しました。")
        return result.get()
    }

    private data class OpenidConfiguration(
        val issuer: String,
        val authorizationEndpoint: String,
        val deviceAuthorizationEndpoint: String,
        val tokenEndpoint: String,
        val userinfoEndpoint: String,
        val revocationEndpoint: String,
        val jwksUri: String,
    )
}