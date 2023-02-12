package com.itomise.admin.controller

import com.itomise.admin.controller.requestModels.JwtLoginResponseModel
import com.itomise.admin.controller.requestModels.LoginRequestModel
import com.itomise.admin.controller.responseModels.MeResponseModel
import com.itomise.admin.domain.security.interfaces.IJwtTokenService
import com.itomise.admin.domain.security.vo.TokenClaim
import com.itomise.admin.module.envConfig
import com.itomise.admin.module.jwtTokenConfig
import com.itomise.admin.usecase.interfaces.auth.ILoginUseCase
import com.itomise.admin.usecase.interfaces.auth.IMeUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun Route.authJwtRouting() {
    val loginUseCase: ILoginUseCase by inject()
    val meUseCase: IMeUseCase by inject()
    val tokenService: IJwtTokenService by inject()

    route("/auth-jwt") {
        post("/login") {
            val request = call.receive<LoginRequestModel>()

            val result = loginUseCase.handle(
                ILoginUseCase.Command(
                    email = request.email,
                    password = request.password
                )
            )

            if (result == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(envConfig.jwt.privateKey))
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

            val token = tokenService.generate(
                config = jwtTokenConfig,
                privateKey = privateKey,
                TokenClaim(
                    name = "userId",
                    value = result.id.toString()
                )
            )

            call.respond(HttpStatusCode.OK, JwtLoginResponseModel(token))
        }

        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()

                val result = meUseCase.handle(userId)

                if (result != null) {
                    call.respond(
                        HttpStatusCode.OK, MeResponseModel(
                            id = result.id,
                            email = result.email,
                            name = result.name,
                            isActive = result.isActive
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}

