package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.usecase.interfaces.auth.ILoginUseCase
import com.itomise.shared.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginInteractor : ILoginUseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()
    private val userService by inject<IUserService>()

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
            name = user.profile?.name?.value,
            email = user.email.value,
        )
    }
}