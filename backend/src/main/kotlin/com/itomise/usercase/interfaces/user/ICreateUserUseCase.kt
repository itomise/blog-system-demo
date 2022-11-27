package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface ICreateUserUseCase {
    suspend fun handle(command: Command): UUID

    data class Command(
        val name: String,
        val email: String,
        val password: String
    )
}