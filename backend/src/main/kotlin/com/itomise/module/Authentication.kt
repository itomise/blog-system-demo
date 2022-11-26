package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.infrastructure.redisClient
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.getEnvConfig
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
    val secretSignKey = hex(getEnvConfig("app.session.signKey"))

    install(Sessions) {
        cookie<UserPrincipal>("user_session", SessionStorageRedis()) {
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

class SessionStorageRedis : SessionStorage {
    override suspend fun write(id: String, value: String) {
        redisClient.set(id, value)
    }

    override suspend fun read(id: String): String {
        return redisClient.get(id) ?: throw NoSuchElementException("Session $id not found")
    }

    override suspend fun invalidate(id: String) {
        redisClient.del(id)
    }
}