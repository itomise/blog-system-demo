package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.LoginRequestModel
import com.itomise.com.itomise.controller.requestModel.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModel.MeResponseModel
import com.itomise.com.itomise.controller.responseModel.SignUpResponseModel
import com.itomise.com.itomise.controller.utils.userSessionPrincipal
import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.module.jwtTokenConfig
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.util.security.token.ITokenService
import com.itomise.com.itomise.util.security.token.TokenClaim
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
    val createUserUseCase: ICreateUserUseCase by inject()
    val tokenService: ITokenService by inject()

    route("/auth-session") {
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
                ICreateUserUseCase.Command(
                    name = request.name,
                    email = request.email,
                    password = request.password
                )
            )

            call.respond(HttpStatusCode.OK, SignUpResponseModel(result))
        }
        get("/logout") {
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

            call.respond(HttpStatusCode.OK, hashMapOf("token" to token))
        }
        get("/hello") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("userId").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $userId! Token is expired at $expiresAt ms.")
        }
        get("/logout") {
            call.sessions.clear<UserPrincipal>()
            call.respond(HttpStatusCode.OK)
        }

    }
    authenticate("auth-jwt") {
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
        get("/hello") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("userId").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
}

