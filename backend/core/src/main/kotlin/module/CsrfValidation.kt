package com.itomise.core.module

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

private const val CUSTOM_HEADER_KEY = "X-Requested-With"
private const val CUSTOM_HEADER_VALUE = "HttpRequest"

class IllegalInvalidCsrfHeaderException() : Exception()

val RequestLoggingPlugin = createApplicationPlugin(name = "RequestLoggingPlugin") {
    onCall { call ->
        if (call.request.httpMethod != HttpMethod.Get) {
            val value = call.request.headers[CUSTOM_HEADER_KEY]
            if (!value.equals(CUSTOM_HEADER_VALUE)) {
                throw IllegalInvalidCsrfHeaderException()
            }
        }
    }
}