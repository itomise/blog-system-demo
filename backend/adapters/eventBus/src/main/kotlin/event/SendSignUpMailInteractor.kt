package com.itomise.eventBus.event

import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.services.UserService
import com.itomise.eventBus.lib.SendGridClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/*
    TODO: 後でちゃんと Event Bus Pattern にする
 */
class SendSignUpMailInteractor : KoinComponent {
    private val userService by inject<UserService>()

    private val subject = "[itomise] アカウント登録確認メール"

    private fun getMailContent(token: String, accountActivateUrl: String, accountSignUpUrl: String) =
        """
登録ありがとうございます。

以下のURLをクリックしてパスワードを設定してください。
$accountActivateUrl?token=$token

メールに記載されているURLの有効期限は24時間です。
有効期限切れの場合は、以下のURLから再度登録を行ってください。
$accountSignUpUrl
""".trimIndent()

    fun handle(user: User, accountActivateUrl: String, accountSignUpUrl: String) {
        val token = userService.generateActivationToken(user)

        SendGridClient.send(
            SendGridClient.SendMailCommand(
                to = user.email,
                subject = subject,
                content = getMailContent(
                    token = token,
                    accountActivateUrl = accountActivateUrl,
                    accountSignUpUrl = accountSignUpUrl
                )
            )
        )
    }
}