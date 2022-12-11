package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface IUpdateAccountUseCase {
    suspend fun handle(command: Command)

    data class Command(
        val id: UUID,
        val name: String
    )
}
