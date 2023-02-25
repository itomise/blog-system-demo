package com.itomise.admin.controller.post

import com.itomise.admin.domain.post.entities.Post
import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createPost() {
    val postRepository by inject<PostRepository>()

    post("/posts") {
        val request = call.receive<CreatePostRequestModel>()
        dbQuery {
            val newPost = Post.new(
                title = request.title,
                content = request.content
            )

            postRepository.save(newPost)
        }

        call.respond(HttpStatusCode.OK)
    }
}

data class CreatePostRequestModel(
    val title: String,
    val content: String,
)
