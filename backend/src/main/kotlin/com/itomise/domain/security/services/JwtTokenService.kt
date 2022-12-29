package com.itomise.com.itomise.domain.security.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.domain.security.vo.TokenConfig
import com.itomise.com.itomise.module.envConfig
import com.itomise.com.itomise.module.jwkProvider
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class JwtTokenService : IJwtTokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        val publicKey = jwkProvider.get(config.publicKeyId).publicKey
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(envConfig.jwt.privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }
}