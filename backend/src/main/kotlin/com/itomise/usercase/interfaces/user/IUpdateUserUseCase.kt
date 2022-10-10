package com.itomise.com.itomise.usercase.interfaces.user

interface IUpdateUserUseCase {
    suspend fun handle(id: String, name: String)
}
