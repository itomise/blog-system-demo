package com.itomise.com.itomise.usercase.interfaces.user

interface IGetUserUseCase {
    fun handle(): GetUserUseCaseOutputDto
}

data class GetUserUseCaseOutputDto(val users: List<GetUserUseCaseOutputDtoUser>)

data class GetUserUseCaseOutputDtoUser(val id: String, val name: String)