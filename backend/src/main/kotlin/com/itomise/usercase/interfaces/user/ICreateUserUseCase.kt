package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface ICreateUserUseCase {
    suspend fun handle(id: UUID, name: String, email: String)
}