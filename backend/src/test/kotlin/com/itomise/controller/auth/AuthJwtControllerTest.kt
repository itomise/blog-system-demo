package com.itomise.controller.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModels.JwtLoginResponseModel
import com.itomise.com.itomise.controller.requestModels.LoginRequestModel
import com.itomise.com.itomise.controller.responseModels.MeResponseModel
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthJwtControllerTest {
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
        client.post("/api/auth-jwt/login") {
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

        client.get("/api/auth-jwt/me") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val body = objectMapper.readValue<MeResponseModel>(this.bodyAsText())

            assertEquals(body.name, name)
            assertEquals(body.email, email)
        }

    }
}