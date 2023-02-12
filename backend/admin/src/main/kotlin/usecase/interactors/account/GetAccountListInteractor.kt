package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.infrastructure.dbQuery
import com.itomise.admin.usecase.interfaces.account.IGetAccountListUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAccountListInteractor : IGetAccountListUseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()

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