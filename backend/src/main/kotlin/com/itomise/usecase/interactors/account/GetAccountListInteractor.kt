package com.itomise.com.itomise.usecase.interactors.account

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.usecase.interfaces.account.IGetAccountListUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class GetAccountListInteractor : IGetAccountListUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(): IGetAccountListUseCase.OutputDto {
        val users = dbQuery {
            userRepository.getList()
        }

        return IGetAccountListUseCase.OutputDto(
            users = users.map { IGetAccountListUseCase.OutputDtoUser(it.id.value, it.name.value, it.email.value) }
        )
    }
}