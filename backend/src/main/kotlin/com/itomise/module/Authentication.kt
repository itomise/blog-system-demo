package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.infrastructure.RedisFactory
import com.itomise.com.itomise.infrastructure.SessionStorageRedis
import com.itomise.getEnvConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.authentication() {
    val isDev = environment.developmentMode
    val secretSignKey = hex(getEnvConfig("app.session.signKey"))

    install(Sessions) {
        cookie<UserPrincipal>("user_session", SessionStorageRedis()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = RedisFactory.SESSION_EXPIRES_DURATION
            cookie.httpOnly = true
            if (!isDev) {
                cookie.secure = true
            }
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }
    }

    install(Authentication) {
        session<UserPrincipal>("auth-session") {
            validate { session ->
                if (session.id.isNotEmpty()) {
                    session
                } else null
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}