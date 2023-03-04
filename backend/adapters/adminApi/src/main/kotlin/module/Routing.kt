package com.itomise.adminApi.module

import com.itomise.adminApi.controller.auth.*
import com.itomise.adminApi.controller.authJwt.authJwtLogin
import com.itomise.adminApi.controller.authJwt.authJwtMe
import com.itomise.adminApi.controller.post.*
import com.itomise.adminApi.controller.user.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.routing() {
    routing {
        route("/api/admin") {
            // auth
            authLogin()
            authSignUp()
            startGoogleOAuth2()
            callbackGoogleOAuth2()
            authSignUpActivate()

            // auth-jwt
            authJwtLogin()

            authenticate("auth-session") {
                // auth
                authMe()
                authLogout()

                // auth-jwt
                authJwtMe()

                // posts
                createPost()
                getPost()
                getListPost()
                updatePost()
                deletePost()
                publishPost()
                unPublishPost()

                // users
                userGetList()
                userCreate()
                userGet()
                userUpdate()
                userDelete()
            }
        }
    }
}
