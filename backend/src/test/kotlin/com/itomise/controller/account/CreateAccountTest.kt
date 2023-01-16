package com.itomise.controller.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModels.CreateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.CreateUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAccountTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client, "05テスト太郎", "05@example.com")

        val res = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("test@test.test")))
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val resBody = objectMapper.readValue<CreateUserResponseModel>(res.bodyAsText())
        
        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(2, res.users.size)
            res.users.find { it.id == resBody.id }?.run {
                assertEquals(resBody.id, this.id)
                assertEquals(null, this.name)
                assertEquals("test@test.test", this.email)
            }
        }
    }

    @Test
    fun `未ログイン状態で叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("04@example.com")))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}