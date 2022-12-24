package com.itomise.com.itomise.usercase.interfaces.account

import java.util.*

interface IGetAccountListUseCase {
    suspend fun handle(): OutputDto

    data class OutputDto(val users: List<OutputDtoUser>)
    data class OutputDtoUser(val id: UUID, val name: String, val email: String)
}


