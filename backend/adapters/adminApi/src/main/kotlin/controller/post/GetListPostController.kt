package com.itomise.adminApi.controller.post

import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.PostRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*


fun Route.getListPost() {
    val postRepository by inject<PostRepository>()


    get("/posts") {
        val posts = dbQuery {
            postRepository.getList()
        }

        call.respond(HttpStatusCode.OK, GetListPostResponseModel(
            posts = posts.map {
                GetPostResponseModel(
                    id = it.id,
                    title = it.title,
                    content = it.content,
                    status = it.status.value,
                    plainContent = it.plainContent
                )
            }
        ))
    }
}

data class GetListPostResponseModel(
    val posts: List<GetPostResponseModel>
)

data class GetPostResponseModel(
    val id: UUID,
    val title: String,
    val content: String,
    val plainContent: String,
    val status: Int,
)