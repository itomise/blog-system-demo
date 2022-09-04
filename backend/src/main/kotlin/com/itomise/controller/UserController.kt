package com.itomise.controller

import com.itomise.com.itomise.usercase.interactors.user.GetUserInteractor
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRouting() {

    val getUserUseCase: IGetUserUseCase by inject()

    routing {
        route("/user") {
            get("") {
                call.respondText("Hello World!")
            }
            post("") {
                val request = call.receive<UserPostRequestType>()

                val result = getUserUseCase.handle()

                val response = UserPostResponseModel(
                    result.id.value,
                    result.name
                )

                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}

data class UserPostRequestType(
    val name: String,
    val type: String?
)

data class UserPostResponseModel(
    val id: String,
    val name: String
)