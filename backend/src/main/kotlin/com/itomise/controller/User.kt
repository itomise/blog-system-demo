package com.itomise.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRouting() {
    routing {
        route("/user") {
            get("") {
                call.respondText("Hello World!")
            }
            post("") {
                val request = call.receive<UserPostRequestType>()

                call.respond(HttpStatusCode.Created, request)
            }
        }
    }
}

data class UserPostRequestType(
    val name: String,
    val type: String?
)