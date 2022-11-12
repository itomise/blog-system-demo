package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.*

fun Application.authentication() {
    val isDev = environment.developmentMode
    val userRepository = getKoinInstance<IUserRepository>()

    install(Sessions) {
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserPrincipal>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24 * 2
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
                if (!session.id.isNullOrBlank()) {
                    val res = dbQuery {
                        userRepository.findByUserId(UserId(UUID.fromString(session.id)))
                    }

                    if (res != null) {
                        UserPrincipal(id = res.id.value.toString())
                    } else null
                } else null
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}