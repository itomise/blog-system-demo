package com.itomise.com.itomise.usercase.interactors.auth

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class LoginInteractor : ILoginUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: ILoginUseCase.Command): ILoginUseCase.OutputDtoUser? {
        val user = dbQuery {
            userRepository.findByEmail(Email(command.email))
        } ?: return null

        return ILoginUseCase.OutputDtoUser(
            id = user.id.value,
            name = user.name,
            email = user.email.value,
        )
    }
}