package com.itomise.com.itomise.usecase.interfaces.post

import java.util.*

interface IUnPublishPostUseCase {
    suspend fun handle(id: UUID)
}