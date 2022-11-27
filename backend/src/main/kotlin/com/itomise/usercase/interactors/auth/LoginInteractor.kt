package com.itomise.com.itomise.usercase.interactors.auth

import com.itomise.com.itomise.domain.auth.UserLoginInfoService
import com.itomise.com.itomise.domain.auth.interfaces.IUserLoginInfoRepository
import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery

class LoginInteractor : ILoginUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userLoginInfoRepository = getKoinInstance<IUserLoginInfoRepository>()

    override suspend fun handle(command: ILoginUseCase.Command): ILoginUseCase.OutputDtoUser? {

        val user = dbQuery {
            val loginUserInfo = userLoginInfoRepository.findByEmail(Email(command.email))
                ?: return@dbQuery null

            val isValidPassword = UserLoginInfoService.checkValidPassword(
                password = command.password,
                userLoginInfo = loginUserInfo
            )
            if (!isValidPassword) return@dbQuery null

            userRepository.findByUserId(loginUserInfo.userId)
        } ?: return null

        return ILoginUseCase.OutputDtoUser(
            id = user.id,
            name = user.name,
            email = user.email.value,
        )
    }
}