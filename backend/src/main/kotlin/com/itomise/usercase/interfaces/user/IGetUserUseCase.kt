package com.itomise.com.itomise.usercase.interfaces.user

interface IGetUserUseCase {
    suspend fun handle(): GetUserUseCaseOutputDto
}

data class GetUserUseCaseOutputDto(val users: List<GetUserUseCaseOutputDtoUser>)

data class GetUserUseCaseOutputDtoUser(val id: String, val name: String)