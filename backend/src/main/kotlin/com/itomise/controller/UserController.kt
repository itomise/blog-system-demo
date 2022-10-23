package com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.CreateUserRequestModel
import com.itomise.com.itomise.controller.requestModel.DeleteUserRequestModel
import com.itomise.com.itomise.controller.requestModel.UpdateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModelUser
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRouting() {

    val getUserUseCase: IGetUserUseCase by inject()
    val createUserUseCase: ICreateUserUseCase by inject()
    val updateUserUseCase: IUpdateUserUseCase by inject()
    val deleteUserUseCase: IDeleteUserUseCase by inject()

    routing {
        route("/user") {
            get("") {
                val result = getUserUseCase.handle()

                val response = GetListUserResponseModel(
                    result.users.map {
                        GetListUserResponseModelUser(it.id, it.name)
                    }
                )

                call.respond(HttpStatusCode.OK, response)
            }

            post("") {
                val request = call.receive<CreateUserRequestModel>()

                createUserUseCase.handle(request.id, request.name, request.email)

                call.respond(HttpStatusCode.OK)
            }

            put("") {
                val request = call.receive<UpdateUserRequestModel>()

                updateUserUseCase.handle(request.id, request.name)

                call.respond(HttpStatusCode.OK)
            }

            delete("") {
                val request = call.receive<DeleteUserRequestModel>()

                deleteUserUseCase.handle(request.id)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
