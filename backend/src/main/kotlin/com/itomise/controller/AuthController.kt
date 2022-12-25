package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.JwtLoginResponseModel
import com.itomise.com.itomise.controller.requestModel.LoginRequestModel
import com.itomise.com.itomise.controller.requestModel.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModel.MeResponseModel
import com.itomise.com.itomise.controller.responseModel.SignUpResponseModel
import com.itomise.com.itomise.controller.utils.userSessionPrincipal
import com.itomise.com.itomise.domain.account.vo.UserPrincipal
import com.itomise.com.itomise.domain.security.interfaces.ITokenService
import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.module.jwtTokenConfig
import com.itomise.com.itomise.usercase.interfaces.account.ICreateAccountUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authRouting() {
    val loginUseCase: ILoginUseCase by inject()
    val meUseCase: IMeUseCase by inject()
    val createUserUseCase: ICreateAccountUseCase by inject()
    val tokenService: ITokenService by inject()

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequestModel>()

            val result = loginUseCase.handle(
                ILoginUseCase.Command(
                    email = request.email,
                    password = request.password
                )
            )

            if (result != null) {
                call.sessions.set(UserPrincipal(id = result.id.toString()))

                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/sign-up") {
            val request = call.receive<SignUpRequestModel>()

            val result = createUserUseCase.handle(
                ICreateAccountUseCase.Command(
                    name = request.name,
                    email = request.email,
                    password = request.password
                )
            )

            call.respond(HttpStatusCode.OK, SignUpResponseModel(result))
        }
        post("/logout") {
            call.sessions.clear<UserPrincipal>()
            call.respond(HttpStatusCode.OK)
        }
        authenticate("auth-session") {
            get("/me") {
                val principal = call.userSessionPrincipal()

                val result = meUseCase.handle(principal.id)

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

