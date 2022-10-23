package com.itomise.com.itomise.usercase.interfaces.user

import java.util.*

interface IGetUserUseCase {
    suspend fun handle(): GetUserUseCaseOutputDto
}

data class GetUserUseCaseOutputDto(val users: List<GetUserUseCaseOutputDtoUser>)

data class GetUserUseCaseOutputDtoUser(val id: UUID, val name: String)