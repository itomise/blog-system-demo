package com.itomise.blogDb.queryService.blog

import com.itomise.blogDb.dao.PostTable
import com.itomise.core.domain.post.vo.PostStatus
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime
import java.util.*

class SearchPublishedPostsQueryService {

    data class Output(
        val id: UUID,
        val title: String,
        val content: String,
        val plainContent: String,
        val publishedAt: LocalDateTime?
    )

    fun handle(limit: Int = 10, offset: Long = 0, query: String): List<Output> {
        return PostTable
            .slice(PostTable.id, PostTable.title, PostTable.content, PostTable.plainContent, PostTable.publishedAt)
            .select {
                PostTable.status eq PostStatus.PUBLISH.value and (
                        PostTable.plainContent like "%$query%" or (PostTable.title like "%$query%")
                        )
            }
            .limit(n = limit, offset = offset)
            .orderBy(PostTable.updatedAt, SortOrder.DESC)
            .map {
                Output(
                    id = it[PostTable.id].value,
                    title = it[PostTable.title],
                    content = it[PostTable.content],
                    plainContent = it[PostTable.plainContent],
                    publishedAt = it[PostTable.publishedAt]
                )
            }
    }
}