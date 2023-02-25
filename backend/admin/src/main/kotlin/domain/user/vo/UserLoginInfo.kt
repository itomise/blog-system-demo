package com.itomise.admin.domain.user.vo

interface UserLoginInfo

enum class UserLoginType(val value: Int) {
    INTERNAL(1),
    EXTERNAL_GOOGLE(2);

    companion object {
        fun get(value: Int) = values().find { it.value == value }
            ?: IllegalArgumentException("指定された UserLoginType が存在しません。 value: $value")
    }
}