package com.itomise.controller.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.cleanUp
import com.itomise.com.itomise.controller.requestModel.LoginRequestModel
import com.itomise.com.itomise.controller.requestModel.SignUpRequestModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import com.itomise.com.itomise.controller.responseModel.SignUpResponseModel
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserListTest {
    @AfterTest
    fun after() = cleanUp()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザー一覧が取得できること`() = appTestApplication {
        val client = createClient {
            install(HttpCookies)
        }

        val signUpResponse = client.post("/auth-session/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        name = "テスト太郎",
                        email = "test@example.com",
                        password = "test"
                    )
                )
            )
        }

        val signUpRes = objectMapper.readValue<SignUpResponseModel>(signUpResponse.bodyAsText())

        client.post("/auth-session/login") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    LoginRequestModel(
                        email = "test@example.com",
                        password = "test"
                    )
                )
            )
        }

        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(1, res.users.size)
            res.users[0].run {
                assertEquals(signUpRes.userId, this.id)
                assertEquals("テスト太郎", this.name)
                assertEquals("test@example.com", this.email)
            }
        }
    }
}