package com.itomise.adminApi.controller.auth

import com.itomise.core.domain.user.vo.UserPrincipal
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authLogout() {
    post("/auth/logout") {
        call.sessions.clear<UserPrincipal>()
        call.respond(HttpStatusCode.OK)
    }
}