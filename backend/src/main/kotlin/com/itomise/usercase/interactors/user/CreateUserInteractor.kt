package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.auth.UserLoginInfo
import com.itomise.com.itomise.domain.auth.UserLoginInfoService
import com.itomise.com.itomise.domain.auth.interfaces.IUserLoginInfoRepository
import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.UserService
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class CreateUserInteractor : ICreateUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userLoginInfoRepository = getKoinInstance<IUserLoginInfoRepository>()

    override suspend fun handle(command: ICreateUserUseCase.Command): UUID {
        val userId = dbQuery {
            val createUserId = UUID.randomUUID()

            val userLoginInfo = UserLoginInfo.generate(
                userId = createUserId,
                email = Email(command.email),
                password = command.password
            )

            val user = User.create(
                id = createUserId,
                name = command.name,
                email = Email(command.email),
            )

            if (UserService().isDuplicateUser(user)) {
                throw IllegalArgumentException("指定されたUsernameは既に使用されています。")
            }
            if (UserLoginInfoService.isDuplicateUser(userLoginInfo)) {
                throw IllegalArgumentException("指定されたEmailは既に使用されています。")
            }

            userRepository.save(user)
            userLoginInfoRepository.save(userLoginInfo)

            return@dbQuery createUserId
        }

        return userId
    }
}