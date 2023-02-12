package com.itomise.admin.domain.account.vo

data class UserExternalLoginInfo(
    val externalServiceType: ExternalServiceType
) : UserLoginInfo {
    enum class ExternalServiceType(val value: Int) {
        GOOGLE(1);

        companion object {
            fun get(value: Int) = values().find { it.value == value }
                ?: throw IllegalArgumentException("invalid ExternalServiceType. $value")
        }
    }
}