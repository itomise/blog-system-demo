package com.itomise.adminApi.controller.auth

import com.itomise.adminApi.module.adminApiEnvConfig
import com.itomise.adminApi.module.jwkProvider
import com.itomise.adminApi.module.jwtTokenConfig
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.services.UserService
import com.itomise.core.domain.user.vo.Email
import com.itomise.core.domain.user.vo.UserLoginType
import com.itomise.core.domain.user.vo.UserPrincipal
import com.itomise.core.lib.google.GoogleAuthentication
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.apache.http.client.utils.URIBuilder
import org.koin.ktor.ext.inject
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

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
                val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminApiEnvConfig.jwt.privateKey))
                val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

                userService.generateActivationToken(
                    user = user,
                    tokenConfig = jwtTokenConfig,
                    publicKey = jwkProvider.get(jwtTokenConfig.publicKeyId).publicKey,
                    privateKey = privateKey
                )
            }

            Pair(user, activateToken)
        } catch (e: BadRequestException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return@get
        }

        val redirectUrl = if (user.isActive) {
            // ログイン状態にしてリダイレクトさせる
            call.sessions.set(UserPrincipal(id = user.id.toString()))
            URIBuilder(adminApiEnvConfig.urls.adminApiRootUrl).build().toURL()
        } else {
            URIBuilder(adminApiEnvConfig.urls.accountActivateUrl)
                .addParameter("token", activateToken)
                .build().toURL()
        }

        call.response.headers.append("Content-Type", "text/html")
        call.response.headers.append("Location", redirectUrl.toString())
        call.respond(HttpStatusCode.Found)
    }
}