package com.itomise.controller.post

import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModels.CreatePostRequestModel
import com.itomise.com.itomise.controller.responseModels.GetListPostResponseModel
import com.itomise.com.itomise.controller.responseModels.GetPostResponseModel
import com.itomise.com.itomise.lib.jackson.JacksonUtil
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPostTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = JacksonUtil.mapper

    @Test
    fun `ポストが取得できること`() = appTestApplication {
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

        val getListRes = client.get("/api/posts")

        assertEquals(HttpStatusCode.OK, getListRes.status)
        val getListResult = objectMapper.readValue<GetListPostResponseModel>(getListRes.bodyAsText())

        assertEquals(1, getListResult.posts.size)
        val post1 = getListResult.posts[0]
        assertEquals("title", post1.title)
        assertEquals("<p>test content</p>", post1.content)

        val getRes = client.get("/api/posts/${post1.id}")
        assertEquals(HttpStatusCode.OK, getRes.status)
        val getResult = objectMapper.readValue<GetPostResponseModel>(getRes.bodyAsText())

        assertEquals("title", getResult.title)
        assertEquals("<p>test content</p>", getResult.content)

        client.post("/api/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreatePostRequestModel(
                        title = "title2",
                        content = "<p>test content2</p>"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        val res2 = client.get("/api/posts")

        assertEquals(HttpStatusCode.OK, res2.status)
        val result2 = objectMapper.readValue<GetListPostResponseModel>(res2.bodyAsText())

        assertEquals(2, result2.posts.size)
        result2.posts[0].run {
            assertEquals("title", this.title)
            assertEquals("<p>test content</p>", this.content)
        }
        result2.posts[1].run {
            assertEquals("title2", this.title)
            assertEquals("<p>test content2</p>", this.content)
        }
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.get("/api/posts") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }

        client.get("/api/posts/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}