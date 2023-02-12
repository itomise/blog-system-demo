package com.itomise.admin.usecase.interfaces.auth

import com.itomise.admin.domain.account.vo.UserId

interface IActivateUserUseCase {
    suspend fun handle(command: Command): UserId

    data class Command(
        val token: String,
        val name: String,
        val password: String?
    )
}