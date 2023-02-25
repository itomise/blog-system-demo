package com.itomise.admin.controller.user

import com.itomise.admin.infrastructure.repositories.user.UserRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userGetList() {
    val userRepository by inject<UserRepository>()

    get("/users") {
        val users = dbQuery {
            userRepository.getList()
        }

        val response = GetListUserResponseModel(
            users.map {
                GetListUserResponseModelUser(it.id, it.profile?.name?.value, it.email.value, it.isActive)
            }
        )

        call.respond(HttpStatusCode.OK, response)
    }
}

data class GetListUserResponseModel(val users: List<GetListUserResponseModelUser>)

data class GetListUserResponseModelUser(val id: UUID, val name: String?, val email: String, val isActive: Boolean)