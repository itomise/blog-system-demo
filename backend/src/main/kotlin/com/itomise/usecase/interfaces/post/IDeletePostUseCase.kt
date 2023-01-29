package com.itomise.com.itomise.usecase.interfaces.post

import java.util.*

interface IDeletePostUseCase {
    suspend fun handle(id: UUID)
}