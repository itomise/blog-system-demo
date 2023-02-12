package com.itomise.admin.usecase.interfaces.account

import java.util.*

interface IDeleteAccountUseCase {
    suspend fun handle(command: Command)

    data class Command(
        val id: UUID
    )
}