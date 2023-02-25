package com.itomise.core.domain.user.vo


enum class EmailValidationStatus(val value: Int) {
    NOT_CONFIRMED(1),
    CONFIRMED(2),
    SUSPENDED(3);

    companion object {
        fun get(value: Int): EmailValidationStatus =
            values().find { it.value == value }
                ?: throw java.lang.IllegalArgumentException("invalid EmailValidationStatus. $value")
    }
}