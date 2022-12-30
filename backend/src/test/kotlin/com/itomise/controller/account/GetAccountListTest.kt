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
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAccountListTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザー一覧が取得できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        val prepareCreateUserRequests = listOf(
            CreateUserRequestModel("01テスト太郎", "01@example.com"),
            CreateUserRequestModel("03テスト太郎", "03@example.com"),
            CreateUserRequestModel("02テスト太郎", "02@example.com"),
            CreateUserRequestModel("04テスト太郎", "04@example.com"),
        )
        val requestUserIdList = mutableListOf<UUID>()

        val user = authSessionUserForTest(client, "05テスト太郎", "05@example.com")

        prepareCreateUserRequests.forEach {
            val res = client.post("/api/users") {
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(it))
            }
            val resBody = objectMapper.readValue<CreateUserResponseModel>(res.bodyAsText())
            requestUserIdList.add(resBody.id)
        }
        // ログインユーザはリストの最後に入れる
        requestUserIdList.add(user.id)

        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(5, res.users.size)
            res.users[0].run {
                assertEquals(requestUserIdList[0], this.id)
                assertEquals("01テスト太郎", this.name)
                assertEquals("01@example.com", this.email)
            }
            res.users[1].run {
                assertEquals(requestUserIdList[2], this.id)
                assertEquals("02テスト太郎", this.name)
                assertEquals("02@example.com", this.email)
            }
            res.users[2].run {
                assertEquals(requestUserIdList[1], this.id)
                assertEquals("03テスト太郎", this.name)
                assertEquals("03@example.com", this.email)
            }
            res.users[3].run {
                assertEquals(requestUserIdList[3], this.id)
                assertEquals("04テスト太郎", this.name)
                assertEquals("04@example.com", this.email)
            }
            res.users[4].run {
                assertEquals(requestUserIdList[4], this.id)
                assertEquals("05テスト太郎", this.name)
                assertEquals("05@example.com", this.email)
            }
        }
    }

    @Test
    fun `未ログイン状態でユーザ一覧を叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}