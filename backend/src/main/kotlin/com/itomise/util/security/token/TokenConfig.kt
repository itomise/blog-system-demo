package com.itomise.com.itomise.util.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val publicKeyId: String
)
