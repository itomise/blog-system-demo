package com.itomise.com.itomise.controller.utils


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.module.envConfig
import com.itomise.com.itomise.module.jwkProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


fun ApplicationCall.userSessionPrincipal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>()
    return principal ?: throw IllegalArgumentException("invalid principal")
}

fun createJwt(userId: String): String {
    val privateKeyString = envConfig.jwt.privateKey
    val issuer = envConfig.jwt.issuer
    val audience = envConfig.jwt.audience
    val myRealm = envConfig.jwt.realm

    val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
}