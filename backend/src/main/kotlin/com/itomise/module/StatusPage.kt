package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.common.exception.CustomIllegalArgumentException
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
        exception<CustomIllegalArgumentException> { call, e ->
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
}