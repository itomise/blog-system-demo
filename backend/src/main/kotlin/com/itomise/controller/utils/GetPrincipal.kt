package com.itomise.com.itomise.controller.utils


import com.itomise.com.itomise.domain.auth.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun ApplicationCall.userSessionPrincipal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>()
    return principal ?: throw IllegalStateException("invalid principal")
}