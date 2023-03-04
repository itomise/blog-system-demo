package com.itomise.blogApi.controller.post

import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.queryService.blog.GetPublishedPostsQueryService
import com.itomise.core.util.removeHtmlTagFromString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.postGetList() {
    val getPublishedPostsQueryService by inject<GetPublishedPostsQueryService>()

    get("/posts") {
        val posts = dbQuery {
            getPublishedPostsQueryService.handle()
        }.map {
            PostGetListResponseModelPost(
                id = it.id,
                title = it.title,
                content = it.content,
                displayContent = removeHtmlTagFromString(it.content),
                publishedAt = it.publishedAt?.toString()
            )
        }

        call.respond(HttpStatusCode.OK, PostGetListResponseModel(posts))
    }
}

data class PostGetListResponseModel(
    val posts: List<PostGetListResponseModelPost>
)

data class PostGetListResponseModelPost(
    val id: UUID,
    val title: String,
    val content: String,
    val displayContent: String,
    val publishedAt: String?
)