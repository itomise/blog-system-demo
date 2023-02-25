package com.itomise.admin.controller.post

import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.updatePost() {
    val postRepository by inject<PostRepository>()

    put("/posts/{postId}") {
        val postId = call.parameters["postId"] ?: throw IllegalArgumentException()
        val request = call.receive<UpdatePostRequestModel>()

        dbQuery {
            val post =
                postRepository.findByPostId(UUID.fromString(postId))
                    ?: throw NotFoundException("指定された PostId は存在しません。")

            val updatedPost = post.updateContent(
                title = request.title,
                content = request.content
            )
            postRepository.save(updatedPost)
        }

        call.respond(HttpStatusCode.OK)
    }
}

data class UpdatePostRequestModel(
    val title: String,
    val content: String,
)