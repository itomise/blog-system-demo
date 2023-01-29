package com.itomise.com.itomise.usecase.interfaces.post

import java.util.*

interface IPublishPostUseCase {
    suspend fun handle(id: UUID)
}