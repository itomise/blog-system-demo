package com.itomise.admin.domain.post.entities

import com.itomise.admin.domain.post.vo.PostId
import com.itomise.admin.domain.post.vo.PostStatus
import java.time.LocalDateTime
import java.util.*

data class Post internal constructor(
    val id: PostId,
    val title: String,
    val content: String,
    val status: PostStatus,
    val publishedAt: LocalDateTime?
) {
    fun updateContent(title: String, content: String) = this.copy(
        title = title,
        content = content
    )

    fun publish() = this.copy(
        status = PostStatus.PUBLISH
    )

    fun unPublish() = this.copy(
        status = PostStatus.UN_PUBLISHED
    )

    companion object {
        fun new(title: String, content: String) = Post(
            id = PostId(UUID.randomUUID()),
            title = title,
            content = content,
            status = PostStatus.UN_PUBLISHED,
            publishedAt = null
        )

        fun from(id: PostId, title: String, content: String, status: PostStatus, publishedAt: LocalDateTime?) = Post(
            id = id,
            title = title,
            content = content,
            status = status,
            publishedAt = publishedAt
        )
    }
}
