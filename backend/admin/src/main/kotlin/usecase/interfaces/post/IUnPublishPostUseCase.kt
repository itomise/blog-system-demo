package com.itomise.admin.usecase.interfaces.post

import java.util.*

interface IUnPublishPostUseCase {
    suspend fun handle(id: UUID)
}