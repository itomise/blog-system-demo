package com.itomise.admin.module

import com.itomise.admin.controller.authJwtRouting
import com.itomise.admin.controller.authRouting
import com.itomise.admin.controller.postRouting
import com.itomise.admin.controller.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routing() {
    routing {
        route("/api/admin") {
            userRouting()
            authRouting()
            authJwtRouting()
            postRouting()
        }
    }
}
