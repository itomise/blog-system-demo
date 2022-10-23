package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.UUID

class DeleteUserInteractor : IDeleteUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(id: UUID) {
        dbQuery {
            val targetUser = userRepository.findByUserId(UserId(id))
            if (targetUser != null) {
                userRepository.delete(targetUser)
            } else {
                throw IllegalArgumentException("指定された userId は存在しません")
            }
        }
    }
}