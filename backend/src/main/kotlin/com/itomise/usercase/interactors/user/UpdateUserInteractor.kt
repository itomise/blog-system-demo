package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class UpdateUserInteractor : IUpdateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(id: String, name: String) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(id))
                ?: throw IllegalArgumentException("指定されたユーザーは存在しません")

            val changedUser = targetUser.changeName(name)
            userRepository.save(changedUser)
        }
    }
}