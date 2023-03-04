package controller.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.user.CreateUserRequestModel
import com.itomise.adminApi.controller.user.CreateUserResponseModel
import com.itomise.adminApi.controller.user.GetListUserResponseModel
import helper.AdminApiTestApplication.appTestApplication
import helper.AdminApiTestApplication.authSessionUserForTest
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CreateUserTest {
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client, "05テスト太郎", "05@example.com")

        val res = client.post("/api/admin/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("test@test.test")))
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val resBody = objectMapper.readValue<CreateUserResponseModel>(res.bodyAsText())

        client.get("/api/admin/users").apply {

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
        client.post("/api/admin/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("04@example.com")))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}