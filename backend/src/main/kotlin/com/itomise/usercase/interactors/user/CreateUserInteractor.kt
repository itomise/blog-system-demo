package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.UserService
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class CreateUserInteractor : ICreateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(id: UUID, name: String, email: String) {
        dbQuery {
            if (UserService().duplicateEmail(Email(email))) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(
                User.create(
                    id = UserId(id),
                    name = name,
                    email = Email(email)
                )
            )
        }
    }
}