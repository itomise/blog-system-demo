package com.itomise.blogApi.controller.post

import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.queryService.blog.SearchPublishedPostsQueryService
import com.itomise.core.util.removeHtmlTagFromString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.koin.ktor.ext.inject

fun Route.postSearch() {
    val searchPublishedPostsQueryService by inject<SearchPublishedPostsQueryService>()
    get("/posts/search") {
        val query = call.request.queryParameters["query"] ?: throw IllegalArgumentException()
        val limit = call.request.queryParameters["limit"] ?: throw IllegalArgumentException()
        val offset = call.request.queryParameters["offset"] ?: throw IllegalArgumentException()

        val result = dbQuery {
            it.addLogger(StdOutSqlLogger)
            searchPublishedPostsQueryService.handle(
                limit = limit.toInt(),
                offset = offset.toLong(),
                query = query
            )
        }

        call.respond(HttpStatusCode.OK, PostGetListResponseModel(
            posts = result.map {
                PostGetListResponseModelPost(
                    id = it.id,
                    title = it.title,
                    content = it.content,
                    displayContent = removeHtmlTagFromString(it.content),
                    publishedAt = it.publishedAt?.toString()
                )
            }
        ))
    }
}

