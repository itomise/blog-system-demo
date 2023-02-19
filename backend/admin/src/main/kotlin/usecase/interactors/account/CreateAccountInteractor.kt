package com.itomise.admin.usecase.interactors.account

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.mail.ISendSignUpMailUseCase
import com.itomise.shared.infrastructure.dbQuery
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CreateAccountInteractor : ICreateAccountUseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()
    private val sendSignUpMailUseCase by inject<ISendSignUpMailUseCase>()
    private val userService by inject<IUserService>()

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