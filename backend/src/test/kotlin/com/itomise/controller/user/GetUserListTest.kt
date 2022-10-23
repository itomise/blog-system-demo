package com.itomise.controller.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.cleanUp
import com.itomise.com.itomise.controller.requestModel.CreateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserListTest {
    @AfterTest
    fun after() = cleanUp()

    @Test
    fun `ユーザーが0の場合空の配列が返ること`() = appTestApplication {
        val objectMapper = jacksonObjectMapper()

        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(0, res.users.size)
        }
    }

    @Test
    fun `ユーザー一覧が取得できること`() = appTestApplication {
        val userId = UUID.randomUUID()

        val objectMapper = jacksonObjectMapper()

        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreateUserRequestModel(
                        id = userId,
                        name = "テスト太郎",
                        email = "test@example.com"
                    )
                )
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)

        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(1, res.users.size)
            res.users[0].run {
                assertEquals(userId, this.id)
                assertEquals("テスト太郎", this.name)
            }
        }
    }
}