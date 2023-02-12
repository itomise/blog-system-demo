package com.itomise.admin.usecase.interfaces.post

import java.util.*

interface IPublishPostUseCase {
    suspend fun handle(id: UUID)
}