package com.itomise.adminApi.controller.auth

import com.itomise.adminApi.module.adminApiEnvConfig
import com.itomise.adminApi.module.jwkProvider
import com.itomise.adminApi.module.jwtTokenConfig
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.services.UserService
import com.itomise.core.domain.user.vo.Email
import com.itomise.eventBus.event.SendSignUpMailInteractor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun Route.authSignUp() {
    val userRepository by inject<UserRepository>()
    val sendSignUpMailUseCase by inject<SendSignUpMailInteractor>()
    val userService by inject<UserService>()

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

            val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminApiEnvConfig.jwt.privateKey))
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

            val token = userService.generateActivationToken(
                user = targetUser,
                tokenConfig = jwtTokenConfig,
                publicKey = jwkProvider.get(jwtTokenConfig.publicKeyId).publicKey,
                privateKey = privateKey
            )
            // 後で EventDispatcher に移す
            sendSignUpMailUseCase.handle(
                user = targetUser,
                accountActivateUrl = adminApiEnvConfig.urls.accountActivateUrl,
                accountSignUpUrl = adminApiEnvConfig.urls.accountSignUpUrl,
                token = token
            )

            targetUser
        }

        call.respond(HttpStatusCode.OK, SignUpResponseModel(user.id))
    }
}

data class SignUpRequestModel(val email: String)

data class SignUpResponseModel(val userId: UUID)
