package com.itomise.core.domain.user.vo

enum class UserOperationType(val value: String) {
    ACTIVATE("1"),
    PASSWORD_RESET("2");

    companion object {
        fun get(value: String) = values().find { it.value == value }
            ?: throw java.lang.IllegalArgumentException("不正な UserOperationTypeです。 value: $value")
    }
}