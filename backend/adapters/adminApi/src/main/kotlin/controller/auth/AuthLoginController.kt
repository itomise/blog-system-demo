package com.itomise.adminApi.controller.auth

import com.itomise.core.domain.user.services.UserService
import com.itomise.core.domain.user.vo.Email
import com.itomise.core.domain.user.vo.UserPrincipal
import com.itomise.blogDb.repository.UserRepository
import com.itomise.blogDb.lib.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authLogin() {
    val userService by inject<UserService>()
    val userRepository by inject<UserRepository>()

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