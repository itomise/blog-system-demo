package com.itomise.admin.controller.user

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.services.UserService
import com.itomise.admin.domain.user.vo.Email
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.admin.usecase.SendSignUpMailInteractor
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userCreate() {
    val sendSignUpMailUseCase by inject<SendSignUpMailInteractor>()
    val userRepository by inject<UserRepository>()
    val userService by inject<UserService>()

    post("/users") {
        val request = call.receive<CreateUserRequestModel>()

        val user = dbQuery {
            val newUser = User.new(
                email = Email(request.email),
            )

            val allUser = userRepository.getList()

            if (userService.isDuplicateUser(allUser, newUser)) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(newUser)

            // TODO: 後で EventDispatcher にする
            sendSignUpMailUseCase.handle(newUser)

            newUser
        }

        call.respond(HttpStatusCode.OK, CreateUserResponseModel(user.id))
    }
}

data class CreateUserRequestModel(val email: String)

data class CreateUserResponseModel(val id: UUID)