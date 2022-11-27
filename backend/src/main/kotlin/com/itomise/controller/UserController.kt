package com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.CreateUserRequestModel
import com.itomise.com.itomise.controller.requestModel.UpdateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.CreateUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModelUser
import com.itomise.com.itomise.controller.userPrincipal
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.userRouting() {

    val getUserUseCase: IGetUserUseCase by inject()
    val createUserUseCase: ICreateUserUseCase by inject()
    val updateUserUseCase: IUpdateUserUseCase by inject()
    val deleteUserUseCase: IDeleteUserUseCase by inject()

    routing {
        route("/users") {
            authenticate("auth-session") {
                get("") {
                    val result = getUserUseCase.handle()

                    val response = GetListUserResponseModel(
                        result.users.map {
                            GetListUserResponseModelUser(it.id, it.name, it.email)
                        }
                    )

                    call.respond(HttpStatusCode.OK, response)
                }

                post("") {
                    val request = call.receive<CreateUserRequestModel>()

                    val userId = createUserUseCase.handle(
                        ICreateUserUseCase.Command(
                            name = request.name,
                            email = request.email,
                            password = request.password
                        )
                    )

                    call.respond(HttpStatusCode.OK, CreateUserResponseModel(userId))
                }

                put("/{userId}") {
                    val request = call.receive<UpdateUserRequestModel>()
                    val userId = call.parameters["userId"] ?: return@put throw IllegalArgumentException()

                    updateUserUseCase.handle(
                        IUpdateUserUseCase.Command(
                            id = UUID.fromString(userId),
                            name = request.name,
                        )
                    )

                    call.respond(HttpStatusCode.OK)
                }

                delete("/{userId}") {
                    val userId = call.parameters["userId"] ?: return@delete throw IllegalArgumentException()
                    val principal = call.userPrincipal()

                    if (userId == principal.id) {
                        throw IllegalArgumentException()
                    }

                    deleteUserUseCase.handle(IDeleteUserUseCase.Command(UUID.fromString(userId)))

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
