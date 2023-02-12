package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.usecase.interfaces.account.IGetAccountListUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery

class GetAccountListInteractor : IGetAccountListUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(): IGetAccountListUseCase.OutputDto {
        val users = dbQuery {
            userRepository.getList()
        }

        return IGetAccountListUseCase.OutputDto(
            users = users.map {
                IGetAccountListUseCase.OutputDtoUser(
                    it.id.value,
                    it.profile?.name?.value,
                    it.email.value,
                    it.isActive
                )
            }
        )
    }
}