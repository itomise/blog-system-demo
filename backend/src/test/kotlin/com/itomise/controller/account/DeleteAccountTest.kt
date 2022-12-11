package com.itomise.controller.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModel.CreateUserRequestModel
import com.itomise.com.itomise.controller.responseModel.CreateUserResponseModel
import com.itomise.com.itomise.controller.responseModel.GetListUserResponseModel
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeleteAccountTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが削除できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        val createRes = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreateUserRequestModel(
                        "test太郎",
                        "test@example.com",
                        "${UUID.randomUUID()}"
                    ),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val createResBody = objectMapper.readValue<CreateUserResponseModel>(createRes.bodyAsText())


        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(2, res.users.size)
            res.users.find { it.id == createResBody.id }?.run {
                assertEquals(createResBody.id, this.id)
                assertEquals("test太郎", this.name)
                assertEquals("test@example.com", this.email)
            }
        }

        client.delete("/api/users/${createResBody.id}").apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(1, res.users.size)
            assertNull(res.users.find { it.id == createResBody.id })
        }
    }

    @Test
    fun `未ログイン状態で叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        client.delete("/api/users/${UUID.randomUUID()}").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }

    @Test
    fun `存在しないユーザーIDで叩くと400になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        authSessionUserForTest(client)

        client.delete("/api/users/${UUID.randomUUID()}").apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }

        client.delete("/api/users/hogehoge").apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }

    @Test
    fun `自分自身を削除できないこと`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        val user = authSessionUserForTest(client)

        client.delete("/api/users/${user.id}").apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }
}