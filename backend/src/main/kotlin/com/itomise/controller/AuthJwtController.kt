package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModels.JwtLoginResponseModel
import com.itomise.com.itomise.controller.requestModels.LoginRequestModel
import com.itomise.com.itomise.controller.responseModels.MeResponseModel
import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.module.jwtTokenConfig
import com.itomise.com.itomise.usecase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.IMeUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

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

            val token = tokenService.generate(
                config = jwtTokenConfig,
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
                            name = result.name
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}

