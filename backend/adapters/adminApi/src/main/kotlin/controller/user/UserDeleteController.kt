package com.itomise.adminApi.controller.user

import com.itomise.adminApi.util.userSessionPrincipal
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.exception.NotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userDelete() {
    val userRepository by inject<UserRepository>()

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