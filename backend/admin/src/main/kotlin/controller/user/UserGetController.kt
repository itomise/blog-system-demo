package com.itomise.admin.controller.user

import com.itomise.admin.infrastructure.repositories.user.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userGet() {
    val userRepository by inject<UserRepository>()
    
    get("/users/{userId}") {
        val userId = call.parameters["userId"]

        val user = dbQuery {
            userRepository.findByUserId(UUID.fromString(userId))
        }

        if (user != null) call.respond(
            HttpStatusCode.OK, GetUserResponseModel(
                id = user.id,
                name = user.profile?.name?.value,
                email = user.email.value,
                isActive = user.isActive
            )
        ) else call.respond(HttpStatusCode.NotFound)
    }
}

data class GetUserResponseModel(val id: UUID, val name: String?, val email: String, val isActive: Boolean)