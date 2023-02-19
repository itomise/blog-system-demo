package com.itomise.blog.module

import com.itomise.blog.controller.postRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routing() {
    routing {
        route("/api/public") {
            postRouting()
        }
    }
}
