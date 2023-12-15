package com.example.sharedcard.email

import java.util.Date
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailMessage {
    private const val SUBJECT = "Подтвердите вход"
    private fun MESSAGE(code: String) = "<p style=\"font-family:Arial,sans-serif;color:#000000;font-size:19px;margin-top:14px;margin-bottom:0\">Здравствуйте!</p>" +
            "<p style=\"font-family:Arial,sans-serif;color:#000000;font-size:14px;line-height:17px;margin-top:30px;margin-bottom:0\">Ваш одноразовый код подтверждения: $code</p>"

    fun send(email: String, code: String) {
        try {
            val props = Properties()
            props.put("mail.smtp.host", "smtp-mail.outlook.com")
            props.put("mail.smtp.port", "587")
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true")
            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(LOGIN, PASSWORD)
                }
            })
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(LOGIN))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
            message.setContent(MESSAGE(code),"text/html; charset=utf-8")
            message.subject = SUBJECT
            message.sentDate = Date()
            Transport.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}