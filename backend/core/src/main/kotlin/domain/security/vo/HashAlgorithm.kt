package com.itomise.core.domain.security.vo

enum class HashAlgorithm(val value: Int) {
    SHA256(1),
    SHA384(2),
    SHA512(3);

    companion object {
        fun get(value: Int): HashAlgorithm =
            values().find { it.value == value } ?: throw IllegalArgumentException("invalid HashAlgorithm. $value")

        fun getRandom(): HashAlgorithm = values().random()
    }
}