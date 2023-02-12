package controller.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.controller.requestModels.CreateUserRequestModel
import com.itomise.admin.controller.requestModels.UpdateUserRequestModel
import com.itomise.admin.controller.responseModels.CreateUserResponseModel
import com.itomise.admin.controller.responseModels.GetListUserResponseModel
import controller.BaseTestApplication
import controller.BaseTestApplication.Companion.appTestApplication
import controller.BaseTestApplication.Companion.authSessionUserForTest
import controller.BaseTestApplication.Companion.cleanup
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class UpdateAccountTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが編集できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        val email = "${UUID.randomUUID()}@example.com"
        val password = "${UUID.randomUUID()}"

        val createRes = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel(email)))
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val createResBody = objectMapper.readValue<CreateUserResponseModel>(createRes.bodyAsText())

        client.post("/activate-for-testing") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    BaseTestApplication.Companion.ActivateTestUserRequest(
                        id = createResBody.id,
                        name = "test太郎",
                        email = email,
                        password = password
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            res.users.find { it.id == createResBody.id }?.run {
                assertEquals(createResBody.id, this.id)
                assertEquals("test太郎", this.name)
                assertEquals(email, this.email)
            }
        }

        client.put("/api/users/${createResBody.id}") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    UpdateUserRequestModel("test太郎1")
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            res.users.find { it.id == createResBody.id }?.run {
                assertEquals(createResBody.id, this.id)
                assertEquals("test太郎1", this.name)
                assertEquals(email, this.email)
            }
        }
    }

    @Test
    fun `未ログイン状態で叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreateUserRequestModel(
                        "04@example.com",
                    ),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }

    @Test
    fun `存在しないUserIdのパスで叩くと400になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        authSessionUserForTest(client)

        client.put("/api/users/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    UpdateUserRequestModel("04テスト太郎"),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }

        client.put("/api/users/hogehoge") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    UpdateUserRequestModel("04テスト太郎"),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }
}