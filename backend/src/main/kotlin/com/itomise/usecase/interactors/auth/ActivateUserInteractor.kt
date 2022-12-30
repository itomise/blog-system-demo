package com.itomise.com.itomise.usecase.interactors.auth

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class ActivateUserInteractor : IActivateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(command: IActivateUserUseCase.Command) {
        val userId =
            userService.getUserIdFromActivationToken(command.token) ?: throw IllegalArgumentException("不正なtokenです。")

        dbQuery {
            val user = userRepository.findByUserId(userId)
                ?: throw IllegalArgumentException("存在しないユーザーIDです。")

            val activatedUser = user.activate(command.password)

            userRepository.save(activatedUser)
        }
    }
}