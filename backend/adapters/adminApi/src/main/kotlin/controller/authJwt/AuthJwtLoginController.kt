package com.itomise.adminApi.controller.authJwt

import com.itomise.adminApi.controller.auth.LoginRequestModel
import com.itomise.adminApi.module.adminApiEnvConfig
import com.itomise.adminApi.module.jwkProvider
import com.itomise.adminApi.module.jwtTokenConfig
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.security.services.JwtTokenService
import com.itomise.core.domain.security.vo.TokenClaim
import com.itomise.core.domain.user.services.UserService
import com.itomise.core.domain.user.vo.Email
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun Route.authJwtLogin() {
    val userRepository by inject<UserRepository>()
    val userService by inject<UserService>()
    val tokenService by inject<JwtTokenService>()

    post("/auth-jwt/login") {
        val request = call.receive<LoginRequestModel>()

        val user = dbQuery {
            val targetUser = userRepository.findByEmail(Email(request.email))
                ?: return@dbQuery null

            val isValidPassword = userService.isValidPassword(
                password = request.password,
                user = targetUser
            )

            if (!isValidPassword) return@dbQuery null

            targetUser
        }

        if (user == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminApiEnvConfig.jwt.privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)


        val token = tokenService.generate(
            config = jwtTokenConfig,
            privateKey = privateKey,
            publicKey = jwkProvider.get(jwtTokenConfig.publicKeyId).publicKey,
            claims = arrayOf(
                TokenClaim(
                    name = "userId",
                    value = user.id.toString()
                )
            )
        )

        call.respond(HttpStatusCode.OK, JwtLoginResponseModel(token))
    }
}

data class JwtLoginResponseModel(val token: String)