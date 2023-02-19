package com.itomise.admin.module

import com.itomise.admin.domain.common.exception.CustomBadRequestException
import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.shared.module.IllegalInvalidCsrfHeaderException
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
        exception<IllegalInvalidCsrfHeaderException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<CustomBadRequestException> { call, e ->
            this@statusPage.log.info("BadRequest : ${e.localizedMessage}")
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
        exception<NotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
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