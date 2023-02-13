package com.itomise.admin.controller

import com.itomise.admin.controller.requestModels.CreateUserRequestModel
import com.itomise.admin.controller.requestModels.UpdateUserRequestModel
import com.itomise.admin.controller.responseModels.CreateUserResponseModel
import com.itomise.admin.controller.responseModels.GetListUserResponseModel
import com.itomise.admin.controller.responseModels.GetListUserResponseModelUser
import com.itomise.admin.controller.responseModels.GetUserResponseModel
import com.itomise.admin.controller.utils.userSessionPrincipal
import com.itomise.admin.usecase.interfaces.account.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userRouting() {

    val getUserListUseCase: IGetAccountListUseCase by inject()
    val getUserUseCase: IGetAccountUseCase by inject()
    val createUserUseCase: ICreateAccountUseCase by inject()
    val updateUserUseCase: IUpdateAccountUseCase by inject()
    val deleteUserUseCase: IDeleteAccountUseCase by inject()

    authenticate("auth-session") {
        route("/users") {
            get("") {
                val result = getUserListUseCase.handle()

                val response = GetListUserResponseModel(
                    result.users.map {
                        GetListUserResponseModelUser(it.id, it.name, it.email, it.isActive)
                    }
                )

                call.respond(HttpStatusCode.OK, response)
            }

            post("") {
                val request = call.receive<CreateUserRequestModel>()

                val userId = createUserUseCase.handle(
                    ICreateAccountUseCase.Command(
                        email = request.email,
                    )
                )

                call.respond(HttpStatusCode.OK, CreateUserResponseModel(userId))
            }

            get("/{userId}") {
                val userId = call.parameters["userId"]

                val user = getUserUseCase.handle(
                    IGetAccountUseCase.Command(
                        userId = UUID.fromString(userId)
                    )
                )

                if (user != null) call.respond(
                    HttpStatusCode.OK, GetUserResponseModel(
                        id = user.id,
                        name = user.name,
                        email = user.email,
                        isActive = user.isActive
                    )
                ) else call.respond(HttpStatusCode.NotFound)
            }

            put("/{userId}") {
                val request = call.receive<UpdateUserRequestModel>()
                val userId = call.parameters["userId"]

                updateUserUseCase.handle(
                    IUpdateAccountUseCase.Command(
                        id = UUID.fromString(userId),
                        name = request.name,
                    )
                )

                call.respond(HttpStatusCode.OK)
            }

            delete("/{userId}") {
                val userId = call.parameters["userId"]
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
