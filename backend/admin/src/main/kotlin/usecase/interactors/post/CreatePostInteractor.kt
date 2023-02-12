package com.itomise.admin.usecase.interactors.post

import com.itomise.admin.domain.post.entities.Post
import com.itomise.admin.domain.post.interfaces.IPostRepository
import com.itomise.admin.usecase.interfaces.post.ICreatePostUseCase
import com.itomise.admin.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CreatePostInteractor : ICreatePostUseCase, KoinComponent {
    private val postRepository by inject<IPostRepository>()

    override suspend fun handle(command: ICreatePostUseCase.Command): UUID {
        return dbQuery {
            val newPost = Post.new(
                title = command.title,
                content = command.content
            )

            postRepository.save(newPost)
            newPost.id.value
        }
    }
}