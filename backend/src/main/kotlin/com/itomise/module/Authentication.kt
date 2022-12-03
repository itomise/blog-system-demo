package com.itomise.com.itomise.module

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.infrastructure.RedisFactory
import com.itomise.com.itomise.infrastructure.SessionStorageRedis
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.concurrent.TimeUnit

lateinit var jwkProvider: JwkProvider

fun Application.authentication() {
    val isDev = environment.developmentMode
    val secretSignKey = hex(envConfig.session.signKey)

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

        val issuer = envConfig.jwt.issuer
        val myRealm = envConfig.jwt.realm
        jwkProvider = JwkProviderBuilder(issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()

        jwt("auth-jwt") {
            realm = myRealm
            verifier(jwkProvider, issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
