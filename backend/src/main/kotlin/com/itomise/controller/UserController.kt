package com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.CreateUserRequestModel
import com.itomise.com.itomise.controller.requestModel.UpdateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.CreateUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModelUser
import com.itomise.com.itomise.controller.utils.userSessionPrincipal
import com.itomise.com.itomise.usercase.interfaces.user.ICreateAccountUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteAccountUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IGetAccountListUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateAccountUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userRouting() {

    val getUserUseCase: IGetAccountListUseCase by inject()
    val createUserUseCase: ICreateAccountUseCase by inject()
    val updateUserUseCase: IUpdateAccountUseCase by inject()
    val deleteUserUseCase: IDeleteAccountUseCase by inject()

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
                    ICreateAccountUseCase.Command(
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
                    IUpdateAccountUseCase.Command(
                        id = UUID.fromString(userId),
                        name = request.name,
                    )
                )

                call.respond(HttpStatusCode.OK)
            }

            delete("/{userId}") {
                val userId = call.parameters["userId"] ?: return@delete throw IllegalArgumentException()
                val principal = call.userSessionPrincipal()

                if (userId == principal.id) {
                    throw IllegalArgumentException()
                }

                deleteUserUseCase.handle(IDeleteAccountUseCase.Command(UUID.fromString(userId)))

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
