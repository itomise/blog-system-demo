package com.itomise.com.itomise.usecase.interactors.post

import com.itomise.com.itomise.domain.post.interfaces.IPostRepository
import com.itomise.com.itomise.domain.post.vo.PostId
import com.itomise.com.itomise.usecase.interfaces.post.IGetPostUseCase
import com.itomise.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class GetPostInteractor : IGetPostUseCase, KoinComponent {
    private val postRepository by inject<IPostRepository>()

    override suspend fun handle(id: UUID): IGetPostUseCase.OutputDto? {
        val post = dbQuery {
            postRepository.findByPostId(PostId(id))
        } ?: return null

        return IGetPostUseCase.OutputDto(
            id = post.id.value,
            title = post.title,
            content = post.content,
            status = post.status.value
        )
    }
}