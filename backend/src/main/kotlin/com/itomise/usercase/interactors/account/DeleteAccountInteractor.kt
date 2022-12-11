package com.itomise.com.itomise.usercase.interactors.account

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteAccountUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

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