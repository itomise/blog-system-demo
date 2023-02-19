package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.domain.account.vo.Username
import com.itomise.admin.domain.common.exception.NotFoundException
import com.itomise.admin.usecase.interfaces.account.IUpdateAccountUseCase
import com.itomise.shared.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateAccountInteractor : IUpdateAccountUseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()

    override suspend fun handle(command: IUpdateAccountUseCase.Command) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(command.id))
                ?: throw NotFoundException("指定されたユーザーは存在しません")

            val changedUser = targetUser.changeProfile(Username(command.name))
            userRepository.save(changedUser)
        }
    }
}