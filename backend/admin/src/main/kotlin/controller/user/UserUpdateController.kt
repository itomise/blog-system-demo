package com.itomise.admin.controller.user

import com.itomise.admin.domain.user.vo.Username
import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.infrastructure.repositories.user.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userUpdate() {
    val userRepository by inject<UserRepository>()

    put("/users/{userId}") {
        val request = call.receive<UpdateUserRequestModel>()
        val userId = call.parameters["userId"]

        dbQuery {
            val targetUser = userRepository.findByUserId(UUID.fromString(userId))
                ?: throw NotFoundException("指定されたユーザーは存在しません")

            val changedUser = targetUser.changeProfile(Username(request.name))
            userRepository.save(changedUser)
        }

        call.respond(HttpStatusCode.OK)
    }
}

data class UpdateUserRequestModel(val name: String)