package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class UpdateUserInteractor : IUpdateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: IUpdateUserUseCase.Command) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(command.id))
                ?: throw IllegalArgumentException("指定されたユーザーは存在しません")

            val changedUser = targetUser.changeName(command.name)
            userRepository.save(changedUser)
        }
    }
}