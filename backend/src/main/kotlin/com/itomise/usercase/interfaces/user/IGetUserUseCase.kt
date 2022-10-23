package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface IGetUserUseCase {
    suspend fun handle(): OutputDto

    data class OutputDto(val users: List<OutputDtoUser>)
    data class OutputDtoUser(val id: UUID, val name: String, val email: String)
}


