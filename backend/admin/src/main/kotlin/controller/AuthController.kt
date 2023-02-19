package com.itomise.admin.controller

import com.itomise.admin.controller.requestModels.ActivateRequestModel
import com.itomise.admin.controller.requestModels.LoginRequestModel
import com.itomise.admin.controller.requestModels.SignUpRequestModel
import com.itomise.admin.controller.responseModels.MeResponseModel
import com.itomise.admin.controller.responseModels.SignUpResponseModel
import com.itomise.admin.controller.utils.userSessionPrincipal
import com.itomise.admin.domain.account.vo.UserPrincipal
import com.itomise.admin.module.adminEnvConfig
import com.itomise.admin.usecase.interfaces.auth.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.apache.http.client.utils.URIBuilder
import org.koin.ktor.ext.inject

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

        route("/sign-up") {
            post {
                val request = call.receive<SignUpRequestModel>()

                val userId = signUpUseCase.handle(
                    ISignUpUseCase.Command(
                        email = request.email,
                    )
                )

                call.respond(HttpStatusCode.OK, SignUpResponseModel(userId = userId))
            }

            post("/activate") {
                val request = call.receive<ActivateRequestModel>()

                val result = activateUserUseCase.handle(
                    IActivateUserUseCase.Command(
                        token = request.token,
                        password = request.password,
                        name = request.name
                    )
                )

                call.sessions.set(UserPrincipal(id = result.value.toString()))

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/google_oauth2") {
            get {
                val result = requestGoogleOAuth2UseCase.handle()

                call.response.cookies.append(
                    Cookie(
                        name = "state",
                        value = result.state,
                        path = "/",
                        maxAge = 60 * 60
                    )
                )

                call.response.headers.append("Content-Type", "text/html")
                call.response.headers.append("Location", result.authenticationURI)
                call.respond(HttpStatusCode.Found)
            }

            get("/callback") {
                val paramState = call.parameters["state"] ?: throw IllegalArgumentException("state パラメータが存在しません。")
                val code = call.parameters["code"] ?: throw IllegalArgumentException("code パラメータが存在しません。")

                val cookieState =
                    call.request.cookies["state"] ?: throw IllegalArgumentException("cookie に state がありません。")
                if (paramState != cookieState) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val result = try {
                    callbackGoogleOAuth2UseCase.handle(code)
                } catch (e: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    return@get
                }
                val redirectUrl = if (result.isActiveUser) {
                    // ログイン状態にしてリダイレクトさせる
                    call.sessions.set(UserPrincipal(id = result.userId.toString()))
                    URIBuilder(adminEnvConfig.urls.adminRootUrl).build().toURL()
                } else {
                    URIBuilder(adminEnvConfig.urls.accountActivateUrl)
                        .addParameter("token", result.activateToken)
                        .build().toURL()
                }

                call.response.headers.append("Content-Type", "text/html")
                call.response.headers.append("Location", redirectUrl.toString())
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
                            name = result.name,
                            isActive = result.isActive
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

