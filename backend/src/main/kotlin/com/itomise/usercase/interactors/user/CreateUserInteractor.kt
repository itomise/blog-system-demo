package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.UserService
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class CreateUserInteractor : ICreateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: ICreateUserUseCase.Command) {
        dbQuery {
            if (UserService().duplicateEmail(Email(command.email))) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(
                User.create(
                    id = UserId(command.id),
                    name = command.name,
                    email = Email(command.email)
                )
            )
        }
    }
}