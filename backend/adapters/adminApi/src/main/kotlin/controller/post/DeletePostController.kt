package com.itomise.adminApi.controller.post

import com.itomise.core.exception.NotFoundException
import com.itomise.blogDb.repository.PostRepository
import com.itomise.blogDb.lib.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.deletePost() {
    val postRepository by inject<PostRepository>()

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