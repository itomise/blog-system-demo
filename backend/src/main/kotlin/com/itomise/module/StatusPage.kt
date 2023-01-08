package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.common.exception.CustomIllegalArgumentException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.net.ConnectException

fun Application.statusPage() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, e ->
            this@statusPage.log.info("BadRequest : ${e.localizedMessage}")
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<IllegalHeaderException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<CustomIllegalArgumentException> { call, e ->
            this@statusPage.log.info("BadRequest : ${e.localizedMessage}")
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
        exception<ConnectException> { call, connectException ->
            this@statusPage.log.error(connectException.message)
            call.respond(HttpStatusCode.InternalServerError)
        }
        exception<Throwable> { call, throwable ->
            this@statusPage.log.error(throwable.message)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}