package com.itomise.admin.usecase.interfaces.account

import java.util.*

interface IGetAccountUseCase {
    suspend fun handle(command: Command): OutputDto?

    data class Command(val userId: UUID)

    data class OutputDto(val id: UUID, val name: String?, val email: String, val isActive: Boolean)
}


