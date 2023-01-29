package com.itomise.com.itomise.usecase.interactors.post

import com.itomise.com.itomise.domain.post.interfaces.IPostRepository
import com.itomise.com.itomise.usecase.interfaces.post.IGetListPostUseCase
import com.itomise.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetListPostInteractor : IGetListPostUseCase, KoinComponent {
    private val postRepository by inject<IPostRepository>()

    override suspend fun handle(): List<IGetListPostUseCase.OutputDto> {
        return dbQuery {
            postRepository.getList()
        }.map {
            IGetListPostUseCase.OutputDto(
                id = it.id.value,
                title = it.title,
                content = it.content,
                status = it.status.value
            )
        }
    }
}