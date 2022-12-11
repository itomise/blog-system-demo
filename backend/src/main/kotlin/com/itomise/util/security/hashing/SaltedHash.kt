package com.itomise.com.itomise.util.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)