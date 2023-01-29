package com.itomise.com.itomise.domain.post.interfaces

import com.itomise.com.itomise.domain.post.entities.Post
import com.itomise.com.itomise.domain.post.vo.PostId

interface IPostRepository {
    suspend fun getList(): List<Post>

    suspend fun findByPostId(postId: PostId): Post?

    suspend fun save(post: Post)

    suspend fun delete(post: Post)
}