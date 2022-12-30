package com.itomise.com.itomise.usecase.interactors.auth

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.usecase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class LoginInteractor : ILoginUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(command: ILoginUseCase.Command): ILoginUseCase.OutputDtoUser? {

        val user = dbQuery {
            val targetUser = userRepository.findByEmail(Email(command.email))
                ?: return@dbQuery null

            val isValidPassword = userService.isValidPassword(
                password = command.password,
                user = targetUser
            )

            if (!isValidPassword) return@dbQuery null

            targetUser
        } ?: return null

        return ILoginUseCase.OutputDtoUser(
            id = user.id.value,
            name = user.name.value,
            email = user.email.value,
        )
    }
}