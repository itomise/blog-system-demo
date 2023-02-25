package com.itomise.core.domain.security.vo

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val publicKeyId: String
)
