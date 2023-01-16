package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModels.ActivateRequestModel
import com.itomise.com.itomise.controller.requestModels.LoginRequestModel
import com.itomise.com.itomise.controller.requestModels.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModels.MeResponseModel
import com.itomise.com.itomise.controller.responseModels.SignUpResponseModel
import com.itomise.com.itomise.controller.utils.userSessionPrincipal
import com.itomise.com.itomise.domain.account.vo.UserPrincipal
import com.itomise.com.itomise.module.envConfig
import com.itomise.com.itomise.usecase.interfaces.auth.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.date.*
import org.koin.ktor.ext.inject
import java.time.OffsetDateTime

fun Route.authRouting() {
    val loginUseCase: ILoginUseCase by inject()
    val meUseCase: IMeUseCase by inject()
    val signUpUseCase: ISignUpUseCase by inject()
    val activateUserUseCase: IActivateUserUseCase by inject()
    val requestGoogleOAuth2UseCase: IRequestGoogleOAuth2UseCase by inject()
    val callbackGoogleOAuth2UseCase: ICallbackGoogleOAuth2UseCase by inject()

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

        route("/google_oauth2") {
            get {
                val result = requestGoogleOAuth2UseCase.handle()

                call.response.cookies.append(
                    Cookie(
                        name = "state",
                        value = result.state,
                        path = "/",
                        expires = GMTDate(OffsetDateTime.now().plusMinutes(10).toEpochSecond()),
                    )
                )

                call.response.headers.append("Content-Type", "text/html")
                call.response.headers.append("Location", result.authenticationURI)
                call.respond(HttpStatusCode.Found)
            }

            get("/callback") {
                val paramState = call.parameters["state"] ?: throw IllegalArgumentException("state パラメータが存在しません。")
                val code = call.parameters["code"] ?: throw IllegalArgumentException("code パラメータが存在しません。")

//                val cookieState =
//                    call.request.cookies["states"] ?: throw IllegalArgumentException("cookie に state がありません。")
//                if (paramState != cookieState) {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@get
//                }

                val result = callbackGoogleOAuth2UseCase.handle(code)
                val redirectUrl = if (result.isActiveUser) {
                    // ログイン状態にしてリダイレクトさせる
                    call.sessions.set(UserPrincipal(id = result.userId.toString()))
                    envConfig.urls.adminRootUrl
                } else {
                    // UserId を JWT に入れてパラメータで渡す
                    envConfig.urls.accountActivateUrl
                }

                call.response.headers.append("Content-Type", "text/html")
                call.response.headers.append("Location", redirectUrl)
                call.respond(HttpStatusCode.Found)
            }
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
            post("/logout") {
                call.sessions.clear<UserPrincipal>()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

