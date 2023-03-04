package com.itomise.blogApi.controller.post

import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.PostRepository
import com.itomise.core.util.removeHtmlTagFromString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.postGet() {
    val postRepository by inject<PostRepository>()

    get("/posts/{postId}") {
        val postId = call.parameters["postId"] ?: throw IllegalArgumentException()

        val post = dbQuery {
            postRepository.findByPostId(UUID.fromString(postId))
        }

        if (post != null) {
            call.respond(
                HttpStatusCode.OK, PostGetListResponseModelPost(
                    id = post.id,
                    title = post.title,
                    content = post.content,
                    displayContent = removeHtmlTagFromString(post.content),
                    publishedAt = post.publishedAt.toString()
                )
            )
        } else {
            call.respond(HttpStatusCode.NotFound)
        }


    }
}

