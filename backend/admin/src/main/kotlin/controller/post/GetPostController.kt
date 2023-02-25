package com.itomise.admin.controller.post.detail

import com.itomise.admin.controller.post.GetPostResponseModel
import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.admin.util.removeHtmlTagFromString
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.getPost() {
    val postRepository = PostRepository()

    get("/posts/{postId}") {
        val postId = call.parameters["postId"] ?: throw IllegalArgumentException()

        val post = dbQuery {
            postRepository.findByPostId(UUID.fromString(postId))
        }

        if (post != null) {
            call.respond(
                HttpStatusCode.OK, GetPostResponseModel(
                    id = post.id,
                    title = post.title,
                    content = post.content,
                    status = post.status.value,
                    displayContent = removeHtmlTagFromString(post.content),
                )
            )
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}