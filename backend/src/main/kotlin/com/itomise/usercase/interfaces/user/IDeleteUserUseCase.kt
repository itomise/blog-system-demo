package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface IDeleteUserUseCase {
    suspend fun handle(id: UUID)
}