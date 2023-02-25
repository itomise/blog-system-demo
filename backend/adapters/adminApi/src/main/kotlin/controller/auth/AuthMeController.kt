package com.itomise.adminApi.controller.auth

import com.itomise.adminApi.util.userSessionPrincipal
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.authMe() {
    val userRepository by inject<UserRepository>()

    get("/auth/me") {
        val principal = call.userSessionPrincipal()

        val user = dbQuery {
            userRepository.findByUserId(UUID.fromString(principal.id))
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
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

data class MeResponseModel(val id: UUID, val name: String?, val email: String, val isActive: Boolean)