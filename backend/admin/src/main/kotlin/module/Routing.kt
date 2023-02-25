package com.itomise.admin.module

import com.itomise.admin.controller.auth.*
import com.itomise.admin.controller.authJwt.authJwtLogin
import com.itomise.admin.controller.authJwt.authJwtMe
import com.itomise.admin.controller.post.*
import com.itomise.admin.controller.user.*
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
