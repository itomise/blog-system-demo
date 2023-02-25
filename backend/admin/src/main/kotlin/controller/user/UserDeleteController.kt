package com.itomise.admin.controller.user

import com.itomise.admin.controller.utils.userSessionPrincipal
import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userDelete() {
    val userRepository = UserRepository()

    delete("/users/{userId}") {
        val userId = call.parameters["userId"]
        val principal = call.userSessionPrincipal()

        if (userId == principal.id) {
            throw IllegalArgumentException()
        }

        dbQuery {
            val targetUser = userRepository.findByUserId(UUID.fromString(userId))
            if (targetUser != null) {
                userRepository.delete(targetUser)
            } else {
                throw NotFoundException("指定された userId は存在しません")
            }
        }

        call.respond(HttpStatusCode.OK)
    }
}