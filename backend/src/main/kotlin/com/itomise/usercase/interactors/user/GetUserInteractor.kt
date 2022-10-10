package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.user.GetUserUseCaseOutputDto
import com.itomise.com.itomise.usercase.interfaces.user.GetUserUseCaseOutputDtoUser
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class GetUserInteractor : IGetUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(): GetUserUseCaseOutputDto {
        val users = dbQuery {
            userRepository.getList()
        }

        return GetUserUseCaseOutputDto(
            users = users.map { GetUserUseCaseOutputDtoUser(it.id.value, it.name) }
        )
    }
}