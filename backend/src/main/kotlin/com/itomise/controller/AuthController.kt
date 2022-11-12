package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModel.LoginRequestModel
import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Application.authRouting() {
    val loginUseCase: ILoginUseCase by inject()
    val meUseCase: IMeUseCase by inject()

    routing {
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
            get("/logout") {
                call.sessions.clear<UserPrincipal>()
                call.respond(HttpStatusCode.OK)
            }
            authenticate("auth-session") {
                get("/me") {
                    val principal = call.userPrincipal()

                    val result = meUseCase.handle(principal.id)

                    if (result != null) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}

fun ApplicationCall.userPrincipal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>()
    return principal ?: throw IllegalArgumentException("invalid principal")
}