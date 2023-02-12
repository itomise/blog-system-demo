package controller.post

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.controller.requestModels.CreatePostRequestModel
import com.itomise.admin.controller.responseModels.GetListPostResponseModel
import controller.BaseTestApplication.Companion.appTestApplication
import controller.BaseTestApplication.Companion.authSessionUserForTest
import controller.BaseTestApplication.Companion.cleanup
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeletePostTest {
    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ポストが削除できること`() = appTestApplication {
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

        client.delete("/api/posts/${post.id}") {
            contentType(ContentType.Application.Json)
        }.run {
            assertEquals(HttpStatusCode.OK, this.status)
        }

        val res2 = client.get("/api/posts")

        assertEquals(HttpStatusCode.OK, res2.status)
        val result2 = objectMapper.readValue<GetListPostResponseModel>(res2.bodyAsText())

        assertEquals(0, result2.posts.size)
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.delete("/api/posts/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}