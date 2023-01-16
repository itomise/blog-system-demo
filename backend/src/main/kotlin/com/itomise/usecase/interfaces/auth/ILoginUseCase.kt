package com.itomise.com.itomise.usecase.interfaces.auth

import java.util.*

interface ILoginUseCase {
    suspend fun handle(command: Command): OutputDtoUser?

    data class Command(
        val email: String,
        val password: String
    )

    data class OutputDtoUser(val id: UUID, val name: String?, val email: String)
}