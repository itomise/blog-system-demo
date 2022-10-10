package com.itomise.com.itomise.usercase.interfaces.user

interface ICreateUserUseCase {
    suspend fun handle(id: String, name: String, email: String)
}