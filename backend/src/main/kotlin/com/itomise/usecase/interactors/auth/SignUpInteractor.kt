package com.itomise.com.itomise.usecase.interactors.auth

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.usecase.interfaces.auth.ISignUpUseCase
import com.itomise.com.itomise.usecase.interfaces.mail.ISendSignUpMailUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class SignUpInteractor : ISignUpUseCase {
    private val sendSignUpMailUseCase = getKoinInstance<ISendSignUpMailUseCase>()
    private val userRepository = getKoinInstance<IUserRepository>()

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