package com.itomise.com.itomise.domain.security.services

import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.DirectEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.util.*

class JwtTokenTokenService : IJwtTokenService {

    override fun generate(
        signatureKey: RSAKey,
        encryptionKey: ByteArray,
        claims: Map<String, String>
    ): String {
        require(encryptionKey.size == 32) { "invalid encryptionKey." }

        // 署名で使用する Signer を生成
        val signer: JWSSigner = RSASSASigner(signatureKey)

        // JWS 形式の JWT 生成
        val signedJwt = try {
            val jwtClaimsSet = JWTClaimsSet
                .Builder()
                .issueTime(Date())

            claims.forEach { (key, value) ->
                jwtClaimsSet.claim(key, value)
            }

            SignedJWT(JWSHeader.Builder(JWSAlgorithm.RS256).build(), jwtClaimsSet.build()).also {
                it.sign(signer)
            }
        } catch (ex: JOSEException) {
            throw IllegalStateException("jwt signature failed")
        }

        // 暗号化で使用する Encryptor 生成
        val encryptor = DirectEncrypter(encryptionKey)

        // JWE 形式の JWT を生成
        return try {
            JWEObject(JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM).build(), Payload(signedJwt)).also {
                it.encrypt(encryptor)
            }.serialize()
        } catch (ex: JOSEException) {
            throw IllegalStateException("jwt encryption failed")
        }
    }

    override fun verify(
        token: String,
        publicKey: RSAKey,
        encryptionKey: ByteArray
    ): Boolean {
        val jweObject = try {
            JWEObject.parse(token)
        } catch (e: Exception) {
            return false
        }

        // デコード
        try {
            jweObject.decrypt(DirectDecrypter(encryptionKey))
        } catch (e: Exception) {
            return false
        }

        // 署名検証
        val jwt = jweObject.payload.toSignedJWT()

        return jwt.verify(RSASSAVerifier(publicKey))
    }
}