package com.itomise.admin.usecase

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.lib.sendgrid.SendGridClient
import com.itomise.admin.module.adminEnvConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SendSignUpMailInteractor : KoinComponent {
    private val userService by inject<IUserService>()

    private val subject = "[itomise] アカウント登録確認メール"

    private fun getMailContent(token: String) =
        """
登録ありがとうございます。

以下のURLをクリックしてパスワードを設定してください。
${adminEnvConfig.urls.accountActivateUrl}?token=$token

メールに記載されているURLの有効期限は24時間です。
有効期限切れの場合は、以下のURLから再度登録を行ってください。
${adminEnvConfig.urls.accountSignUpUrl}
""".trimIndent()

    fun handle(user: User) {
        val token = userService.generateActivationToken(user)

        SendGridClient.send(
            SendGridClient.SendMailCommand(
                to = user.email,
                subject = subject,
                content = getMailContent(token)
            )
        )
    }
}