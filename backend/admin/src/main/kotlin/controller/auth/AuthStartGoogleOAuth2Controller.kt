package com.itomise.admin.controller.auth

import com.itomise.admin.lib.google.GoogleAuthentication
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigInteger
import java.security.SecureRandom

fun Route.startGoogleOAuth2() {
    get {
        val state = BigInteger(130, SecureRandom()).toString(32)

        val authenticationURI = GoogleAuthentication.createOpenConnectAuthURI(state)

        call.response.cookies.append(
            Cookie(
                name = "state",
                value = state,
                path = "/",
                maxAge = 60 * 60
            )
        )

        call.response.headers.append("Content-Type", "text/html")
        call.response.headers.append("Location", authenticationURI)
        call.respond(HttpStatusCode.Found)
    }
}