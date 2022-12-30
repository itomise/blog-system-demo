package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModels.ActivateRequestModel
import com.itomise.com.itomise.controller.requestModels.LoginRequestModel
import com.itomise.com.itomise.controller.requestModels.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModels.MeResponseModel
import com.itomise.com.itomise.controller.responseModels.SignUpResponseModel
import com.itomise.com.itomise.controller.utils.userSessionPrincipal
import com.itomise.com.itomise.domain.account.vo.UserPrincipal
import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.IMeUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.ISignUpUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authRouting() {
    val loginUseCase: ILoginUseCase by inject()
    val meUseCase: IMeUseCase by inject()
    val createUserUseCase: ICreateAccountUseCase by inject()
    val tokenService: IJwtTokenService by inject()
    val signUpUseCase: ISignUpUseCase by inject()
    val activateUserUseCase: IActivateUserUseCase by inject()

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

            val userId = signUpUseCase.handle(
                ISignUpUseCase.Command(
                    name = request.name,
                    email = request.email,
                )
            )

            call.respond(HttpStatusCode.OK, SignUpResponseModel(userId = userId))
        }

        post("/sign-up/activate") {
            val request = call.receive<ActivateRequestModel>()

            activateUserUseCase.handle(
                IActivateUserUseCase.Command(
                    token = request.token,
                    password = request.password
                )
            )

            call.respond(HttpStatusCode.OK)
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
}

