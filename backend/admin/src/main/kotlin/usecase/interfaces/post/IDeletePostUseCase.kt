package com.itomise.admin.usecase.interfaces.post

import java.util.*

interface IDeletePostUseCase {
    suspend fun handle(id: UUID)
}