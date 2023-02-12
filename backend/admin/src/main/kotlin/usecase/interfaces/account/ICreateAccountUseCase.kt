package com.itomise.admin.usecase.interfaces.account

import java.util.*

interface ICreateAccountUseCase {
    suspend fun handle(command: Command): UUID

    data class Command(
        val email: String,
    )
}