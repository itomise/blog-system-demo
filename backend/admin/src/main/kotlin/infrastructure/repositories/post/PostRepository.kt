package com.itomise.admin.infrastructure.repositories.post

import com.itomise.admin.domain.post.entities.Post
import com.itomise.admin.domain.post.vo.PostId
import com.itomise.admin.domain.post.vo.PostStatus
import com.itomise.admin.infrastructure.dao.PostTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class PostRepository {
    private fun resultRowToPostEntity(row: ResultRow): Post {
        return Post.from(
            id = row[PostTable.id].value,
            title = row[PostTable.title],
            content = row[PostTable.content],
            status = PostStatus.get(row[PostTable.status]),
            publishedAt = row.getOrNull(PostTable.publishedAt)
        )
    }

    suspend fun getList(): List<Post> {
        return PostTable
            .selectAll()
            .orderBy(PostTable.updatedAt, SortOrder.DESC)
            .map(::resultRowToPostEntity)
    }

    suspend fun findByPostId(postId: PostId): Post? {
        return PostTable
            .select(PostTable.id eq postId)
            .map(::resultRowToPostEntity)
            .firstOrNull()
    }

    suspend fun save(post: Post) {
        val isExists = findByPostId(post.id) != null
        if (isExists) {
            PostTable.update({
                PostTable.id eq post.id
            }) { s ->
                s[title] = post.title
                s[content] = post.content
                s[status] = post.status.value
                s[updatedAt] = LocalDateTime.now()
                s[publishedAt] = post.publishedAt
            }
        } else {
            PostTable.insert { s ->
                s[title] = post.title
                s[content] = post.content
                s[status] = post.status.value
                s[publishedAt] = post.publishedAt
            }
        }
    }

    suspend fun delete(post: Post) {
        PostTable.deleteWhere {
            PostTable.id eq post.id
        }
    }
}