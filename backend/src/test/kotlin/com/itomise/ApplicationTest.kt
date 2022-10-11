package com.itomise

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `ユーザー一覧が取得できること`() = testApplication {
        client.get("/user").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}