package com.itomise.com.itomise.domain.security.interfaces

import com.nimbusds.jose.jwk.RSAKey
import java.security.PrivateKey

interface INestedJwtTokenService {
    fun generate(
        signatureKey: PrivateKey,
        encryptionKey: ByteArray,
        claims: Map<String, String>
    ): String

    fun verify(
        token: String,
        publicKey: RSAKey,
        encryptionKey: ByteArray
    ): Boolean
}