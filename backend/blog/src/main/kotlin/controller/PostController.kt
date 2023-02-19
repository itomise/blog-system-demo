package com.itomise.blog.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postRouting() {
    route("/posts") {
        get {
            call.respond(HttpStatusCode.OK, "OK")
        }
    }
}