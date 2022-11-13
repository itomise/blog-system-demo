package com.itomise.com.itomise.usercase.interfaces.auth

import java.util.*

interface IMeUseCase {
    suspend fun handle(userId: String): OutputDtoUser?

    data class OutputDtoUser(val id: UUID, val name: String, val email: String)
}