package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.usecase.interfaces.account.IGetAccountUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery

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
            email = user.email.value,
            isActive = user.isActive
        ) else null
    }
}