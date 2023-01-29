package com.itomise.com.itomise.usecase.interactors.post

import com.itomise.com.itomise.domain.common.exception.NotFoundException
import com.itomise.com.itomise.domain.post.interfaces.IPostRepository
import com.itomise.com.itomise.domain.post.vo.PostId
import com.itomise.com.itomise.usecase.interfaces.post.IUnPublishPostUseCase
import com.itomise.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UnPublishPostInteractor : IUnPublishPostUseCase, KoinComponent {
    private val postRepository by inject<IPostRepository>()

    override suspend fun handle(id: UUID) {
        dbQuery {
            val post =
                postRepository.findByPostId(PostId(id)) ?: throw NotFoundException("指定された PostId は存在しません。 $id")

            val unPublishedPost = post.unPublish()

            postRepository.save(unPublishedPost)
        }
    }
}