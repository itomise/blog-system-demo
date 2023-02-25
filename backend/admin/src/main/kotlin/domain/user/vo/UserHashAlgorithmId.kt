package com.itomise.admin.domain.user.vo

import com.itomise.admin.domain.security.vo.HashAlgorithm

@Suppress("SENSELESS_COMPARISON")
enum class UserHashAlgorithmId(val value: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    init {
        // 便宜上 nullable だが取得できることを保証するため
        require(HashAlgorithm.get(value) !== null)
    }

    companion object {
        fun get(value: Int): UserHashAlgorithmId =
            values().find { it.value == value } ?: throw IllegalArgumentException("invalid HashAlgorithm. $value")
    }
}