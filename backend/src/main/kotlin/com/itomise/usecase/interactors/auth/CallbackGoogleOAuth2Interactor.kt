package com.itomise.com.itomise.usecase.interactors.auth

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserLoginType
import com.itomise.com.itomise.lib.google.GoogleAuthentication
import com.itomise.com.itomise.usecase.interfaces.auth.ICallbackGoogleOAuth2UseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import io.ktor.server.plugins.*

class CallbackGoogleOAuth2Interactor : ICallbackGoogleOAuth2UseCase {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val userService = getKoinInstance<IUserService>()

    override suspend fun handle(code: String): ICallbackGoogleOAuth2UseCase.OutputDto {
        val googleUserInfo = GoogleAuthentication.getGoogleUserInfoByCode(code)

        val user = dbQuery {
            val newUser = User.new(
                email = Email(googleUserInfo.email),
                loginType = UserLoginType.EXTERNAL_GOOGLE
            )

            val savedUser = userRepository.findByEmail(newUser.email)

            if (savedUser != null) {
                if (savedUser.loginType != UserLoginType.EXTERNAL_GOOGLE) {
                    throw BadRequestException("このメールアドレスは既にGoogle以外のログイン情報で登録されています。")
                }
                savedUser
            } else {
                userRepository.save(newUser)
                newUser
            }
        }

        return ICallbackGoogleOAuth2UseCase.OutputDto(
            userId = user.id.value,
            isActiveUser = user.isActive,
            activateToken = if (user.isActive) null else {
                userService.generateActivationToken(user)
            }
        )
    }
}