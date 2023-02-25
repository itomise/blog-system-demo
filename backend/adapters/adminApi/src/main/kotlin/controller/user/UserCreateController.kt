package com.itomise.adminApi.controller.user

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

            val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminApiEnvConfig.jwt.privateKey))
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

            val token = userService.generateActivationToken(
                user = newUser,
                tokenConfig = jwtTokenConfig,
                publicKey = jwkProvider.get(jwtTokenConfig.publicKeyId).publicKey,
                privateKey = privateKey
            )
            // TODO: 後で EventDispatcher にする
            sendSignUpMailUseCase.handle(
                user = newUser,
                accountActivateUrl = adminApiEnvConfig.urls.accountActivateUrl,
                accountSignUpUrl = adminApiEnvConfig.urls.accountSignUpUrl,
                token = token
            )

            newUser
        }

        call.respond(HttpStatusCode.OK, CreateUserResponseModel(user.id))
    }
}

data class CreateUserRequestModel(val email: String)

data class CreateUserResponseModel(val id: UUID)