package com.itomise.admin.controller.authJwt

import com.itomise.admin.controller.auth.MeResponseModel
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.authJwtMe() {
    val userRepository by inject<UserRepository>()

    get("/auth-jwt/me") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val user = dbQuery {
            userRepository.findByUserId(UUID.fromString(userId))
        }

        if (user != null) {
            call.respond(
                HttpStatusCode.OK, MeResponseModel(
                    id = user.id,
                    email = user.email.value,
                    name = user.profile?.name?.value,
                    isActive = user.isActive
                )
            )
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
