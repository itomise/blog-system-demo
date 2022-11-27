package com.itomise.com.itomise.domain.auth


enum class HashAlgorithm(val id: Int) {
    FIRST(1),
    SECOND(2);

    companion object {
        fun get(id: Int): HashAlgorithm =
            values().find { it.id == id } ?: throw IllegalArgumentException("invalid HashAlgorithm. $id")

        fun getRandom(): HashAlgorithm = values().random()
    }
}

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