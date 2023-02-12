package com.itomise.admin.domain.account.vo

import java.util.*

data class UserId(val value: UUID) {
    companion object {
        fun new() = UserId(UUID.randomUUID())
    }
}
