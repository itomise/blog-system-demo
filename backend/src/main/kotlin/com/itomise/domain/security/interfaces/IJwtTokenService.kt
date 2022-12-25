package com.itomise.com.itomise.domain.security.interfaces

import com.nimbusds.jose.jwk.RSAKey

interface IJwtTokenService {
    fun generate(
        signatureKey: RSAKey,
        encryptionKey: ByteArray,
        claims: Map<String, String>
    ): String
}