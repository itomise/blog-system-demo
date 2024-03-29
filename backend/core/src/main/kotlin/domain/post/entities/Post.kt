package com.itomise.core.domain.post.entities

import com.itomise.core.domain.post.vo.PostId
import com.itomise.core.domain.post.vo.PostStatus
import java.time.LocalDateTime
import java.util.*

data class Post internal constructor(
    val id: PostId,
    val title: String,
    val content: String,
    val plainContent: String,
    val status: PostStatus,
    val publishedAt: LocalDateTime?
) {
    fun updateContent(title: String, content: String) = this.copy(
        title = title,
        content = content,
        plainContent = removeHtmlTag(content)
    )

    fun publish() = this.copy(
        status = PostStatus.PUBLISH,
        publishedAt = LocalDateTime.now()
    )

    fun unPublish() = this.copy(
        status = PostStatus.UN_PUBLISHED
    )

    companion object {
        private fun removeHtmlTag(content: String) = content.replace(Regex("<.*?>"), " ").trim()

        fun new(title: String, content: String) = Post(
            id = UUID.randomUUID(),
            title = title,
            content = content,
            plainContent = removeHtmlTag(content),
            status = PostStatus.UN_PUBLISHED,
            publishedAt = null
        )

        fun from(
            id: PostId,
            title: String,
            content: String,
            plainContent: String,
            status: PostStatus,
            publishedAt: LocalDateTime?
        ) = Post(
            id = id,
            title = title,
            content = content,
            plainContent = plainContent,
            status = status,
            publishedAt = publishedAt
        )
    }
}
