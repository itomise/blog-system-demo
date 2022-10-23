package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface IUpdateUserUseCase {
    suspend fun handle(id: UUID, name: String)
}
