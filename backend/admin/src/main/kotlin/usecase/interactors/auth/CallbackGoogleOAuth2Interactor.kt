package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.domain.account.vo.UserLoginType
import com.itomise.admin.lib.google.GoogleAuthentication
import com.itomise.admin.usecase.interfaces.auth.ICallbackGoogleOAuth2UseCase
import com.itomise.shared.infrastructure.dbQuery
import io.ktor.server.plugins.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CallbackGoogleOAuth2Interactor : ICallbackGoogleOAuth2UseCase, KoinComponent {
    private val userRepository by inject<IUserRepository>()
    private val userService by inject<IUserService>()

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