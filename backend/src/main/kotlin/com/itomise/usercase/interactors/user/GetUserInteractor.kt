package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class GetUserInteractor : IGetUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(): IGetUserUseCase.OutputDto {
        val users = dbQuery {
            userRepository.getList()
        }

        return IGetUserUseCase.OutputDto(
            users = users.map { IGetUserUseCase.OutputDtoUser(it.id.value, it.name, it.email.value) }
        )
    }
}