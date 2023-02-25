package com.itomise.admin.controller.post

import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.admin.util.removeHtmlTagFromString
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.getPost() {
    val postRepository by inject<PostRepository>()

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