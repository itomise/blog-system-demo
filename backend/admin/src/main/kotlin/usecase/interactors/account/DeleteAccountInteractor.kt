package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.usecase.interfaces.account.IDeleteAccountUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery

class DeleteAccountInteractor : IDeleteAccountUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

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