package com.itomise.admin.controller.utils


import com.itomise.admin.domain.user.vo.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun ApplicationCall.userSessionPrincipal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>()
    return principal ?: throw IllegalStateException("invalid principal")
}