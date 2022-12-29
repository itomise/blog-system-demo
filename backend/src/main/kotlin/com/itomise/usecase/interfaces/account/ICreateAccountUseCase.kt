package com.itomise.com.itomise.usercase.interfaces.account

import java.util.*

interface ICreateAccountUseCase {
    suspend fun handle(command: Command): UUID

    data class Command(
        val name: String,
        val email: String,
        val password: String
    )
}