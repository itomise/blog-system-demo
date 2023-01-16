package com.itomise.com.itomise.usecase.interactors.account

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.usecase.interfaces.account.IGetAccountUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class GetAccountInteractor : IGetAccountUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: IGetAccountUseCase.Command): IGetAccountUseCase.OutputDto? {
        val userId = UserId(command.userId)
        val user = dbQuery {
            userRepository.findByUserId(userId)
        }

        return if (user != null) IGetAccountUseCase.OutputDto(
            id = user.id.value,
            name = user.profile?.name?.value,
            email = user.email.value
        ) else null
    }
}