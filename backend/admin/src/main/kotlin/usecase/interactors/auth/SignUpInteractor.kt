package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.infrastructure.dbQuery
import com.itomise.admin.usecase.interfaces.auth.ISignUpUseCase
import com.itomise.admin.usecase.interfaces.mail.ISendSignUpMailUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class SignUpInteractor : ISignUpUseCase, KoinComponent {
    private val sendSignUpMailUseCase by inject<ISendSignUpMailUseCase>()
    private val userRepository by inject<IUserRepository>()

    override suspend fun handle(command: ISignUpUseCase.Command): UUID {
        val user = dbQuery {
            val newUser = User.new(
                email = Email(command.email)
            )

            val savedUser = userRepository.findByEmail(newUser.email)

            if (savedUser != null) {
                // 既にユーザーが存在しているかつ、非Activeな場合 (active token 取り直しのケース)
                if (!savedUser.isActive) {
                    return@dbQuery savedUser
                } else {
                    // 既にアクティブな場合はエラー
                    throw IllegalArgumentException("既に認証済みのユーザーです。")
                }
            } else {
                // ユーザーが存在しない場合は新規保存する
                userRepository.save(newUser)
                return@dbQuery newUser
            }
        }

        sendSignUpMailUseCase.handle(user)

        return user.id.value
    }
}