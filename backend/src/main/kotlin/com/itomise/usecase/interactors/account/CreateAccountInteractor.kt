package com.itomise.com.itomise.usecase.interactors.account

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.com.itomise.usecase.interfaces.mail.ISendSignUpMailUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class CreateAccountInteractor : ICreateAccountUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val sendSignUpMailUseCase = getKoinInstance<ISendSignUpMailUseCase>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(command: ICreateAccountUseCase.Command): UUID {
        val user = dbQuery {
            val newUser = User.new(
                email = Email(command.email),
            )

            val allUser = userRepository.getList()

            if (userService.isDuplicateUser(allUser, newUser)) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(newUser)

            return@dbQuery newUser
        }

        sendSignUpMailUseCase.handle(user)

        return user.id.value
    }
}