package com.itomise.admin.domain.security.vo

data class SaltedHash(
    val hash: String,
    val salt: String,
    val algorithm: HashAlgorithm
)