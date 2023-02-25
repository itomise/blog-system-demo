package com.itomise.admin.controller.auth

import com.itomise.admin.domain.account.services.UserService
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.domain.account.vo.UserPrincipal
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authLogin() {
    val userService = UserService()
    val userRepository = UserRepository()

    post("/auth/login") {
        val request = call.receive<LoginRequestModel>()

        val user = dbQuery {
            val targetUser = userRepository.findByEmail(Email(request.email))
                ?: throw IllegalArgumentException()

            val isValidPassword = userService.isValidPassword(
                password = request.password,
                user = targetUser
            )

            if (!isValidPassword) throw IllegalArgumentException()

            targetUser
        }

        call.sessions.set(UserPrincipal(id = user.id.toString()))
        call.respond(HttpStatusCode.OK)
    }
}

data class LoginRequestModel(val email: String, val password: String)