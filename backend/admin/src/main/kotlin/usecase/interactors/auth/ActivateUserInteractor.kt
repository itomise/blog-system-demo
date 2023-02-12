package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.domain.account.vo.UserLoginType
import com.itomise.admin.domain.account.vo.Username
import com.itomise.admin.domain.common.exception.CustomBadRequestException
import com.itomise.admin.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery

class ActivateUserInteractor : IActivateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(command: IActivateUserUseCase.Command): UserId {
        val userId = try {
            userService.getUserIdFromActivationToken(command.token)
        } catch (e: IllegalArgumentException) {
            throw CustomBadRequestException(e.message.toString())
        }

        val activatedUser = dbQuery {
            val user = userRepository.findByUserId(userId)
                ?: throw IllegalArgumentException("存在しないユーザーIDです。")

            val activatedUser = if (user.loginType == UserLoginType.INTERNAL) {
                require(command.password != null)

                user.activateAsInternal(
                    name = Username(command.name),
                    password = command.password
                )
            } else {
                user.activateAsExternal(
                    name = Username(command.name)
                )
            }

            userRepository.save(activatedUser)
            activatedUser
        }

        return activatedUser.id
    }
}