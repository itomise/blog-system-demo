package com.itomise.admin.controller.auth

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.services.UserService
import com.itomise.admin.domain.user.vo.Email
import com.itomise.admin.domain.user.vo.UserLoginType
import com.itomise.admin.domain.user.vo.UserPrincipal
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.admin.lib.google.GoogleAuthentication
import com.itomise.admin.module.adminEnvConfig
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.apache.http.client.utils.URIBuilder
import org.koin.ktor.ext.inject

fun Route.callbackGoogleOAuth2() {
    val userRepository by inject<UserRepository>()
    val userService by inject<UserService>()

    get("/auth/google_oauth2/callback") {
        val paramState = call.parameters["state"] ?: throw IllegalArgumentException("state パラメータが存在しません。")
        val code = call.parameters["code"] ?: throw IllegalArgumentException("code パラメータが存在しません。")

        val cookieState =
            call.request.cookies["state"] ?: throw IllegalArgumentException("cookie に state がありません。")
        if (paramState != cookieState) {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        val (user, activateToken) = try {
            val googleUserInfo = GoogleAuthentication.getGoogleUserInfoByCode(code)

            val user = dbQuery {
                val newUser = User.new(
                    email = Email(googleUserInfo.email),
                    loginType = UserLoginType.EXTERNAL_GOOGLE
                )

                val savedUser = userRepository.findByEmail(newUser.email)

                if (savedUser != null) {
                    if (savedUser.loginType != UserLoginType.EXTERNAL_GOOGLE) {
                        throw BadRequestException("このメールアドレスは既にGoogle以外のログイン情報で登録されています。")
                    }
                    savedUser
                } else {
                    userRepository.save(newUser)
                    newUser
                }
            }

            val activateToken = if (user.isActive) null else {
                userService.generateActivationToken(user)
            }

            Pair(user, activateToken)
        } catch (e: BadRequestException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return@get
        }

        val redirectUrl = if (user.isActive) {
            // ログイン状態にしてリダイレクトさせる
            call.sessions.set(UserPrincipal(id = user.id.toString()))
            URIBuilder(adminEnvConfig.urls.adminRootUrl).build().toURL()
        } else {
            URIBuilder(adminEnvConfig.urls.accountActivateUrl)
                .addParameter("token", activateToken)
                .build().toURL()
        }

        call.response.headers.append("Content-Type", "text/html")
        call.response.headers.append("Location", redirectUrl.toString())
        call.respond(HttpStatusCode.Found)
    }
}