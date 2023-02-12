package com.itomise.admin.module

import com.itomise.admin.controller.authJwtRouting
import com.itomise.admin.controller.authRouting
import com.itomise.admin.controller.postRouting
import com.itomise.admin.controller.userRouting
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
            authJwtRouting()
            postRouting()
        }
    }
}
