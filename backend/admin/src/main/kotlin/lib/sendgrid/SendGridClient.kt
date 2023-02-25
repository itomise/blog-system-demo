package com.itomise.admin.lib.sendgrid

import com.itomise.admin.module.adminEnvConfig
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import java.io.IOException


object SendGridClient {
    fun send(command: SendMailCommand) {
        val from = Email("no-reply@itomise.com")
        val subject = command.subject
        val to = Email(command.to.value)
        val content = Content("text/plain", command.content)
        val mail = Mail(from, subject, to, content)
        val sg = SendGrid(adminEnvConfig.sendGrid.apiKey)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            sg.api(request)
        } catch (ex: IOException) {
            throw ex
        }
    }

    data class SendMailCommand(
        val to: com.itomise.admin.domain.user.vo.Email,
        val subject: String,
        val content: String
    )
}
