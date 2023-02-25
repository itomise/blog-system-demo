package controller.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.user.CreateUserRequestModel
import com.itomise.adminApi.controller.user.CreateUserResponseModel
import com.itomise.adminApi.controller.user.GetListUserResponseModel
import helper.KtorTestApplication.appTestApplication
import helper.KtorTestApplication.authSessionUserForTest
import helper.KtorTestApplication.cleanup
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class DeleteUserTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが削除できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        val createRes = client.post("/api/adminApi/users") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreateUserRequestModel(
                        "test@example.com",
                    ),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val createResBody = objectMapper.readValue<CreateUserResponseModel>(createRes.bodyAsText())


        client.get("/api/adminApi/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(2, res.users.size)
            res.users.find { it.id == createResBody.id }?.run {
                assertEquals(createResBody.id, this.id)
                assertEquals(null, this.name)
                assertEquals("test@example.com", this.email)
            }
        }

        client.delete("/api/adminApi/users/${createResBody.id}").apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/adminApi/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(1, res.users.size)
            assertNull(res.users.find { it.id == createResBody.id })
        }
    }

    @Test
    fun `未ログイン状態で叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        client.delete("/api/adminApi/users/${UUID.randomUUID()}").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }

    @Test
    fun `存在しないユーザーIDで叩くと404になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        authSessionUserForTest(client)

        client.delete("/api/adminApi/users/${UUID.randomUUID()}").apply {
            assertEquals(HttpStatusCode.NotFound, this.status)
        }
    }

    @Test
    fun `不正なユーザーIDで叩くと400になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        authSessionUserForTest(client)

        client.delete("/api/adminApi/users/hogehoge").apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }

    @Test
    fun `自分自身を削除できないこと`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        val user = authSessionUserForTest(client)

        client.delete("/api/adminApi/users/${user.id}").apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }
}