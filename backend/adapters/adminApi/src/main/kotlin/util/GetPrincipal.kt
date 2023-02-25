package com.itomise.adminApi.util

import com.itomise.core.domain.user.vo.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userSessionPrincipal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>()
    return principal ?: throw IllegalStateException("invalid principal")
}