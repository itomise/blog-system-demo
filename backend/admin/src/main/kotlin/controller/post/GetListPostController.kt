package com.itomise.admin.controller.post

import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.admin.util.removeHtmlTagFromString
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*


fun Route.getListPost() {
    val postRepository = PostRepository()


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
                    displayContent = removeHtmlTagFromString(it.content),
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
    val displayContent: String,
    val status: Int,
)