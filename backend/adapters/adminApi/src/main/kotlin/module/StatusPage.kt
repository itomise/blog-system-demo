package com.itomise.adminApi.module

import com.itomise.core.exception.CustomBadRequestException
import com.itomise.core.exception.NotFoundException
import com.itomise.core.module.IllegalInvalidCsrfHeaderException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.net.ConnectException

fun Application.statusPage() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, e ->
            call.application.environment.log.info("BadRequest : ${e.localizedMessage}")
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<IllegalInvalidCsrfHeaderException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<CustomBadRequestException> { call, e ->
            call.application.environment.log.info("BadRequest : ${e.localizedMessage}")
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
        exception<NotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<ConnectException> { call, connectException ->
            call.application.environment.log.error(connectException.message)
            call.respond(HttpStatusCode.InternalServerError)
        }
        exception<Throwable> { call, throwable ->
            call.application.environment.log.error(throwable.message)
            call.respond(HttpStatusCode.InternalServerError)
        }
        exception<Exception> { call, exception ->
            call.application.environment.log.error(exception.message)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}