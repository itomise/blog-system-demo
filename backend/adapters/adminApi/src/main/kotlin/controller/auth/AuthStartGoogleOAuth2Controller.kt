package com.itomise.adminApi.controller.auth

import com.itomise.core.lib.google.GoogleOAuth2Authentication
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.math.BigInteger
import java.security.SecureRandom

fun Route.startGoogleOAuth2() {
    val googleOAuth2Authentication by inject<GoogleOAuth2Authentication>()

    get("/auth/google_oauth2") {
        val state = BigInteger(130, SecureRandom()).toString(32)

        val authenticationURI = googleOAuth2Authentication.createOpenConnectAuthURI(state)

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