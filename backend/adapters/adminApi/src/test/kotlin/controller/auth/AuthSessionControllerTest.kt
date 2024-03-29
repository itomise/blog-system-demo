package controller.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.auth.LoginRequestModel
import com.itomise.adminApi.controller.auth.MeResponseModel
import com.itomise.adminApi.controller.auth.SignUpRequestModel
import com.itomise.adminApi.controller.auth.SignUpResponseModel
import helper.AdminApiTestApplication
import helper.AdminApiTestApplication.appTestApplication
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthSessionControllerTest {
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `サインアップーログインが正しく行えること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        val name = "テスト太郎"
        val email = "${UUID.randomUUID()}@example.com"
        val password = UUID.randomUUID()

        val signUpRes = client.post("/api/admin/auth/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        email = email,
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val signUpResBody = objectMapper.readValue<SignUpResponseModel>(signUpRes.bodyAsText())

        client.post("/activate-for-testing") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    AdminApiTestApplication.ActivateTestUserRequest(
                        id = signUpResBody.userId,
                        name = name,
                        email = email,
                        password = password.toString()
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.post("/api/admin/auth/login") {
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

        client.get("/api/admin/auth/me").apply {
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

        client.get("/api/admin/auth/me").apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}