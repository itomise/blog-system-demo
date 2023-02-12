package com.itomise.admin.usecase.interactors.mail

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.lib.sendgrid.SendGridClient
import com.itomise.admin.module.envConfig
import com.itomise.admin.usecase.interfaces.mail.ISendSignUpMailUseCase
import com.itomise.admin.util.getKoinInstance

class SendSignUpMailInteractor : ISendSignUpMailUseCase {
    private val userService = getKoinInstance<IUserService>()

    private val subject = "[itomise] アカウント登録確認メール"

    private fun getMailContent(token: String) =
        """
登録ありがとうございます。

以下のURLをクリックしてパスワードを設定してください。
${envConfig.urls.accountActivateUrl}?token=$token

メールに記載されているURLの有効期限は24時間です。
有効期限切れの場合は、以下のURLから再度登録を行ってください。
${envConfig.urls.accountSignUpUrl}
""".trimIndent()

    override fun handle(user: User) {
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