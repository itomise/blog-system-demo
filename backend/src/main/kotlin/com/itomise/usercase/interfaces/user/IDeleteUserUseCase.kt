package com.itomise.com.itomise.usercase.interfaces.user

interface IDeleteUserUseCase {
    suspend fun handle(id: String)
}