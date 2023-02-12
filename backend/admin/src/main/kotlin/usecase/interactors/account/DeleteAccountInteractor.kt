package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.infrastructure.dbQuery
import com.itomise.admin.usecase.interfaces.account.IDeleteAccountUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteAccountInteractor : IDeleteAccountUseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()

    override suspend fun handle(command: IDeleteAccountUseCase.Command) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(command.id))
            if (targetUser != null) {
                userRepository.delete(targetUser)
            } else {
                throw IllegalArgumentException("指定された userId は存在しません")
            }
        }
    }
}