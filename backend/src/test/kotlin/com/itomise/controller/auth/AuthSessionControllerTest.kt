package com.itomise.controller.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.com.itomise.controller.requestModel.LoginRequestModel
import com.itomise.com.itomise.controller.requestModel.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModel.MeResponseModel
import com.itomise.com.itomise.controller.responseModel.SignUpResponseModel
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthSessionControllerTest {
    @AfterTest
    fun after() = BaseTestApplication.cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `サインアップーログインが正しく行えること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        val name = "テスト太郎"
        val email = "${UUID.randomUUID()}@example.com"
        val password = UUID.randomUUID()

        val signUpRes = client.post("/auth-session/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        name = name,
                        email = email,
                        password = password.toString()
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val signUpResBody = objectMapper.readValue<SignUpResponseModel>(signUpRes.bodyAsText())


        client.post("/auth-session/login") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    LoginRequestModel(
                        email = email,
                        password = password.toString()
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/auth-session/me").apply {
            assertEquals(HttpStatusCode.OK, this.status)
            val body = objectMapper.readValue<MeResponseModel>(this.bodyAsText())

            assertEquals(signUpResBody.userId, body.id)
            assertEquals(name, body.name)
            assertEquals(email, body.email)
        }
    }

    @Test
    fun `未ログイン状態でMeを叩くとUnauthorizedになること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        client.get("/auth-session/me").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}