package com.itomise.controller.post

import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.BaseTestApplication.Companion.appTestApplication
import com.itomise.BaseTestApplication.Companion.authSessionUserForTest
import com.itomise.BaseTestApplication.Companion.cleanup
import com.itomise.com.itomise.controller.requestModels.CreatePostRequestModel
import com.itomise.com.itomise.controller.responseModels.GetListPostResponseModel
import com.itomise.com.itomise.domain.post.vo.PostStatus
import com.itomise.com.itomise.lib.jackson.JacksonUtil
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreatePostTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = JacksonUtil.mapper

    @Test
    fun `ポストが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        client.post("/api/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreatePostRequestModel(
                        title = "タイトル",
                        content = "<p>テストコンテンツ</p>"
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        client.get("/api/posts").run {
            assertEquals(HttpStatusCode.OK, this.status)
            val result = objectMapper.readValue<GetListPostResponseModel>(this.bodyAsText())

            assertEquals(1, result.posts.size)
            result.posts.forEach {
                assertEquals("タイトル", it.title)
                assertEquals("<p>テストコンテンツ</p>", it.content)
                assertEquals(PostStatus.DRAFT.value, it.status)
            }
        }
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.post("/api/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    objectMapper.writeValueAsString(
                        CreatePostRequestModel(
                            title = "タイトル",
                            content = "<p>テストコンテンツ</p>"
                        )
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}