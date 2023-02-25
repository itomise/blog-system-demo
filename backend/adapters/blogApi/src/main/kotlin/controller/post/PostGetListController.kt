package com.itomise.blogApi.controller.post

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postGetList() {
    get("/posts") {
        call.respond(HttpStatusCode.OK, "OK")
    }
}