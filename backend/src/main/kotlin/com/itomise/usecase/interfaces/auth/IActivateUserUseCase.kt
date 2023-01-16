package com.itomise.com.itomise.usecase.interfaces.auth

interface IActivateUserUseCase {
    suspend fun handle(command: Command)

    data class Command(
        val token: String,
        val name: String,
        val password: String
    )
}