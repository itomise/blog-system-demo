package com.itomise.com.itomise.usecase.interactors.account

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.account.vo.Username
import com.itomise.com.itomise.usecase.interfaces.account.IUpdateAccountUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class UpdateAccountInteractor : IUpdateAccountUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: IUpdateAccountUseCase.Command) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(command.id))
                ?: throw IllegalArgumentException("指定されたユーザーは存在しません")


            val changedUser = targetUser.changeProfile(Username(command.name))
            userRepository.save(changedUser)
        }
    }
}