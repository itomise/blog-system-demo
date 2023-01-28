package com.itomise.com.itomise.usecase.interfaces.auth

import com.itomise.com.itomise.domain.account.vo.UserId

interface IActivateUserUseCase {
    suspend fun handle(command: Command): UserId

    data class Command(
        val token: String,
        val name: String,
        val password: String?
    )
}