package com.itomise.admin.domain.security.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.itomise.admin.domain.security.vo.TokenClaim
import com.itomise.admin.domain.security.vo.TokenConfig
import com.itomise.admin.module.jwkProvider
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

class JwtTokenService {
    fun generate(config: TokenConfig, privateKey: PrivateKey, vararg claims: TokenClaim): String {
        val publicKey = jwkProvider.get(config.publicKeyId).publicKey

        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }

    fun verify(config: TokenConfig, token: String): DecodedJWT? {
        val publicKey = jwkProvider.get(config.publicKeyId).publicKey

        val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey, null)

        val verifier = JWT.require(algorithm)
            .withIssuer(config.issuer)
            .build()

        return try {
            verifier.verify(token)
        } catch (e: Throwable) {
            null
        }
    }
}