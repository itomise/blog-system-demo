package com.itomise.com.itomise.module

import com.itomise.com.itomise.controller.authRouting
import com.itomise.controller.userRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Application.routing() {
    routing {
        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
        route("/api") {
            userRouting()
            authRouting()
        }
    }
}
