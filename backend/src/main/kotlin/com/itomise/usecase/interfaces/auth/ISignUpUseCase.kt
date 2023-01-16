package com.itomise.com.itomise.usecase.interfaces.auth

import java.util.*

interface ISignUpUseCase {
    suspend fun handle(command: Command): UUID

    data class Command(
        val email: String,
    )
}