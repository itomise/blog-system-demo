package com.itomise.core.domain.security.services

import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.DirectEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.PrivateKey
import java.util.*

class NestedJwtTokenTokenService {

    fun generate(
        signatureKey: PrivateKey,
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

    fun verify(
        token: String,
        publicKey: RSAKey,
        encryptionKey: ByteArray
    ): JWTClaimsSet? {
        val jweObject = try {
            JWEObject.parse(token)
        } catch (e: Exception) {
            return null
        }

        // デコード
        try {
            jweObject.decrypt(DirectDecrypter(encryptionKey))
        } catch (e: Exception) {
            return null
        }

        // 署名検証
        val jwt = jweObject.payload.toSignedJWT()

        if (!jwt.verify(RSASSAVerifier(publicKey))) return null

        return jwt.jwtClaimsSet
    }
}