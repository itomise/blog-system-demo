package com.itomise.adminApi.controller.auth

import com.itomise.adminApi.module.jwkProvider
import com.itomise.adminApi.module.jwtTokenConfig
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.user.services.UserService
import com.itomise.core.domain.user.vo.UserLoginType
import com.itomise.core.domain.user.vo.UserPrincipal
import com.itomise.core.domain.user.vo.Username
import com.itomise.core.exception.CustomBadRequestException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authSignUpActivate() {
    val userService by inject<UserService>()
    val userRepository by inject<UserRepository>()

    post("/auth/sign-up/activate") {
        val request = call.receive<ActivateRequestModel>()

        val userId = try {
            userService.getUserIdFromActivationToken(
                token = request.token,
                tokenConfig = jwtTokenConfig,
                publicKey = jwkProvider.get(jwtTokenConfig.publicKeyId).publicKey
            )
        } catch (e: IllegalArgumentException) {
            throw CustomBadRequestException(e.message.toString())
        }

        val activatedUser = dbQuery {
            val user = userRepository.findByUserId(userId)
                ?: throw IllegalArgumentException("存在しないユーザーIDです。")

            val activatedUser = if (user.loginType == UserLoginType.INTERNAL) {
                require(request.password != null)

                // TODO: internal/externalの分岐もモデルの中でやりたい
                user.activateAsInternal(
                    name = Username(request.name),
                    password = request.password
                )
            } else {
                user.activateAsExternal(
                    name = Username(request.name)
                )
            }

            userRepository.save(activatedUser)
            activatedUser
        }

        // ログイン状態にする
        call.sessions.set(UserPrincipal(id = activatedUser.id.toString()))

        call.respond(HttpStatusCode.OK)
    }
}

data class ActivateRequestModel(val name: String, val token: String, val password: String?)