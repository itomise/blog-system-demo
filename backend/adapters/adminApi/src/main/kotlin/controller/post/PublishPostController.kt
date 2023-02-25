package com.itomise.adminApi.controller.post

import com.itomise.core.exception.NotFoundException
import com.itomise.blogDb.repository.PostRepository
import com.itomise.blogDb.lib.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.publishPost() {
    val postRepository by inject<PostRepository>()

    put("/posts/{postId}/publish") {
        val postId = call.parameters["postId"] ?: throw IllegalArgumentException()
        val request = call.receive<PublishPostRequestModel>()

        dbQuery {
            val post =
                postRepository.findByPostId(UUID.fromString(postId))
                    ?: throw NotFoundException("指定された PostId は存在しません。")

            val updatedPost = post.updateContent(
                title = request.title,
                content = request.content
            ).publish()

            postRepository.save(updatedPost)
        }

        call.respond(HttpStatusCode.OK)
    }
}

data class PublishPostRequestModel(
    val title: String,
    val content: String,
)