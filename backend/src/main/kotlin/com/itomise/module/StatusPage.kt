package com.itomise.com.itomise.module

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPage() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<IllegalHeaderException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}