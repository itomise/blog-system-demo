package com.itomise.admin.usecase.interactors.post

import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.domain.post.interfaces.IPostRepository
import com.itomise.admin.domain.post.vo.PostId
import com.itomise.admin.usecase.interfaces.post.IUpdatePostUseCase
import com.itomise.admin.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdatePostInteractor : IUpdatePostUseCase, KoinComponent {
    private val postRepository by inject<IPostRepository>()

    override suspend fun handle(command: IUpdatePostUseCase.Command) {
        dbQuery {
            val post =
                postRepository.findByPostId(PostId(command.id)) ?: throw NotFoundException("指定された PostId は存在しません。")

            val updatedPost = post.updateContent(
                title = command.title,
                content = command.content
            )
            postRepository.save(updatedPost)
        }
    }
}