package com.itomise.controller.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itomise.BaseTestApplication
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.com.itomise.controller.requestModels.SignUpRequestModel
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SignUpInteractorTest {
    @AfterTest
    fun after() = BaseTestApplication.cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザー作成できること`() = appTestApplication {
        client.post("/api/auth/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        email = "${UUID.randomUUID()}@test.test"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
    }

    @Test
    fun `Emailが空文字だとBadRequestになること`() = appTestApplication {
        client.post("/api/auth/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    SignUpRequestModel(
                        email = ""
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, this.status)
        }
    }
}