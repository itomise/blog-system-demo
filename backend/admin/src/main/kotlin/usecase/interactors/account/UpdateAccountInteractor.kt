package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.domain.account.vo.Username
import com.itomise.admin.usecase.interfaces.account.IUpdateAccountUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery

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