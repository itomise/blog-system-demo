package com.itomise.controller.post

import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModels.CreatePostRequestModel
import com.itomise.com.itomise.controller.requestModels.UpdatePostRequestModel
import com.itomise.com.itomise.controller.responseModels.GetListPostResponseModel
import com.itomise.com.itomise.lib.jackson.JacksonUtil
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdatePostTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = JacksonUtil.mapper

    @Test
    fun `ポストが編集できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        client.post("/api/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreatePostRequestModel(
                        title = "title",
                        content = "<p>test content</p>"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        val res1 = client.get("/api/posts")

        assertEquals(HttpStatusCode.OK, res1.status)
        val result = objectMapper.readValue<GetListPostResponseModel>(res1.bodyAsText())

        assertEquals(1, result.posts.size)
        val post = result.posts[0]
        assertEquals("title", post.title)
        assertEquals("<p>test content</p>", post.content)

        client.put("/api/posts/${post.id}") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    UpdatePostRequestModel(
                        title = "updated title",
                        content = "<p>updated test content</p>"
                    )
                )
            )
        }.run {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        val res2 = client.get("/api/posts")

        assertEquals(HttpStatusCode.OK, res2.status)
        val result2 = objectMapper.readValue<GetListPostResponseModel>(res2.bodyAsText())

        assertEquals(1, result2.posts.size)
        val post2 = result2.posts[0]
        assertEquals("updated title", post2.title)
        assertEquals("<p>updated test content</p>", post2.content)
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.put("/api/posts/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    objectMapper.writeValueAsString(
                        UpdatePostRequestModel(
                            title = "updated title",
                            content = "<p>updated test content</p>"
                        )
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}