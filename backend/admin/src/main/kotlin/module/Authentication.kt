package com.itomise.admin.module

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.itomise.admin.domain.user.vo.UserPrincipal
import com.itomise.admin.domain.security.vo.TokenConfig
import com.itomise.admin.infrastructure.RedisFactory
import com.itomise.admin.infrastructure.SessionStorageRedis
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.concurrent.TimeUnit

lateinit var jwkProvider: JwkProvider
lateinit var jwtTokenConfig: TokenConfig

fun Application.authentication() {
    val isDev = environment.developmentMode
    val secretSignKey = hex(adminEnvConfig.session.signKey)

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

    jwtTokenConfig = TokenConfig(
        issuer = adminEnvConfig.jwt.issuer,
        audience = adminEnvConfig.jwt.audience,
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        publicKeyId = adminEnvConfig.jwt.publicKeyId
    )

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

        jwkProvider = JwkProviderBuilder(adminEnvConfig.jwt.issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()

        jwt("auth-jwt") {
            realm = adminEnvConfig.jwt.realm
            verifier(jwkProvider, adminEnvConfig.jwt.issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
