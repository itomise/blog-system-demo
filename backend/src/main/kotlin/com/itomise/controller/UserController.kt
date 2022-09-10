package com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.user.UpdateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.user.GetUserListResponseModel
import com.itomise.com.itomise.controller.responseModel.user.GetUserResponseModel
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
                val result = getUserUseCase.handle()

                val response = GetUserListResponseModel(
                    result.map {
                        GetUserResponseModel(it.id.value, it.name)
                    }
                )

                call.respond(HttpStatusCode.OK, response)
            }
            post("") {
                val request = call.receive<UpdateUserRequestModel>()

                call.respond(HttpStatusCode.Created, request)
            }
        }
    }
}
