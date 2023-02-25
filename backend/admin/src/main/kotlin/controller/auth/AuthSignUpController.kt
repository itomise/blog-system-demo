package com.itomise.admin.controller.auth

import com.itomise.admin.domain.user.entities.User
import com.itomise.admin.domain.user.vo.Email
import com.itomise.admin.infrastructure.repositories.user.UserRepository
import com.itomise.admin.usecase.SendSignUpMailInteractor
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.authSignUp() {
    val userRepository by inject<UserRepository>()
    val sendSignUpMailUseCase by inject<SendSignUpMailInteractor>()

    post("/auth/sign-up") {
        val request = call.receive<SignUpRequestModel>()

        val user = dbQuery {
            val email = Email(request.email)
            val savedUser = userRepository.findByEmail(email)

            val targetUser = if (savedUser != null) {
                // 既にユーザーが存在しているかつ、非Activeな場合 (active token 取り直しのケース)
                if (savedUser.isActive) {
                    // 既にアクティブな場合はエラー
                    throw IllegalArgumentException()
                }

                savedUser
            } else {
                // ユーザーが存在しない場合は新規保存する
                val newUser = User.new(email = email)

                userRepository.save(newUser)
                newUser
            }

            // 後で EventDispatcher に移す
            sendSignUpMailUseCase.handle(targetUser)

            targetUser
        }

        call.respond(HttpStatusCode.OK, SignUpResponseModel(user.id))
    }
}

data class SignUpRequestModel(val email: String)

data class SignUpResponseModel(val userId: UUID)
