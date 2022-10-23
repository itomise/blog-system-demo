package com.itomise.com.itomise.usercase.interfaces.user

import com.itomise.com.itomise.domain.user.vo.UserId

interface ICreateUserUseCase {
    suspend fun handle(command: Command): UserId

    data class Command(
        val name: String,
        val email: String
    )
}