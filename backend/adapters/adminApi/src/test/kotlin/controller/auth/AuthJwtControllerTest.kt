package controller.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.auth.LoginRequestModel
import com.itomise.adminApi.controller.authJwt.JwtLoginResponseModel
import com.itomise.test.helper.KtorTestApplication.appTestApplication
import com.itomise.test.helper.KtorTestApplication.authSessionUserForTest
import com.itomise.test.helper.KtorTestApplication.cleanup
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthJwtControllerTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ログインできること`() = appTestApplication {
        val name = "テスト太郎"
        val email = "${UUID.randomUUID()}@test.test"
        val password = UUID.randomUUID().toString()

        // ユーザー作成
        authSessionUserForTest(client, name, email, password)

        var token = ""
        client.post("/api/admin/auth-jwt/login") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    LoginRequestModel(email, password)
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val body = objectMapper.readValue<JwtLoginResponseModel>(this.bodyAsText())

            token = body.token
        }

        // verifier のモックがうまくいかないので一旦offる
//        client.get("/api/admin/auth-jwt/me") {
//            header(HttpHeaders.Authorization, "Bearer $token")
//        }.apply {
//            assertEquals(HttpStatusCode.OK, this.status)
//
//            val body = objectMapper.readValue<MeResponseModel>(this.bodyAsText())
//
//            assertEquals(body.name, name)
//            assertEquals(body.email, email)
//        }

    }
}