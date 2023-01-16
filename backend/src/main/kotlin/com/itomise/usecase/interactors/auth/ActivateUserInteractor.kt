package com.itomise.com.itomise.usecase.interactors.auth

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Username
import com.itomise.com.itomise.domain.common.exception.CustomIllegalArgumentException
import com.itomise.com.itomise.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class ActivateUserInteractor : IActivateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(command: IActivateUserUseCase.Command) {
        val userId = try {
            userService.getUserIdFromActivationToken(command.token)
        } catch (e: IllegalArgumentException) {
            throw CustomIllegalArgumentException(e.message.toString())
        }

        dbQuery {
            val user = userRepository.findByUserId(userId)
                ?: throw IllegalArgumentException("存在しないユーザーIDです。")

            val activatedUser = user.activateAsInternal(
                name = Username(command.name),
                password = command.password
            )

            userRepository.save(activatedUser)
        }
    }
}