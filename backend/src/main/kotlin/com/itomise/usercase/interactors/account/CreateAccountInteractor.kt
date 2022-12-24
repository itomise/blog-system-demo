package com.itomise.com.itomise.usercase.interactors.account

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.services.UserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.Username
import com.itomise.com.itomise.usercase.interfaces.account.ICreateAccountUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class CreateAccountInteractor : ICreateAccountUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(command: ICreateAccountUseCase.Command): UUID {
        val userId = dbQuery {
            val newUser = User.new(
                name = Username(command.name),
                email = Email(command.email),
                password = command.password
            )

            if (UserService().isDuplicateUserId(newUser.id)) {
                throw IllegalArgumentException("指定されたUsernameは既に使用されています。")
            }
            if (UserService().isDuplicateUserEmail(newUser.email)) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(newUser)

            return@dbQuery newUser.id
        }

        return userId.value
    }
}