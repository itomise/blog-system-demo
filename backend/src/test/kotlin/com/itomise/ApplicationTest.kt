package com.itomise

import com.itomise.BaseTestApplication.Companion.baseTestApplication
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `ユーザー一覧が取得できること`() = baseTestApplication {
        client.get("/user").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}