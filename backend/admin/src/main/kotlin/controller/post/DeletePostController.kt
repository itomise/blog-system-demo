package com.itomise.admin.controller.post.detail

import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.deletePost() {
    val postRepository = PostRepository()

    delete("/posts/{postId}") {
        val postId = call.parameters["postId"] ?: throw IllegalArgumentException()

        dbQuery {
            val post = postRepository.findByPostId(UUID.fromString(postId))
                ?: throw NotFoundException("指定された PostId は存在しません。")

            postRepository.delete(post)
        }

        call.respond(HttpStatusCode.OK)
    }
}