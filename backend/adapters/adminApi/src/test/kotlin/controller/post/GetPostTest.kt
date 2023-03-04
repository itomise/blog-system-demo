package controller.post

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.post.CreatePostRequestModel
import com.itomise.adminApi.controller.post.GetListPostResponseModel
import com.itomise.adminApi.controller.post.GetPostResponseModel
import helper.AdminApiTestApplication.appTestApplication
import helper.AdminApiTestApplication.authSessionUserForTest
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetPostTest {
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ポストが取得できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        client.post("/api/admin/posts") {
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

        val getListRes = client.get("/api/admin/posts")

        assertEquals(HttpStatusCode.OK, getListRes.status)
        val getListResult = objectMapper.readValue<GetListPostResponseModel>(getListRes.bodyAsText())

        assertEquals(1, getListResult.posts.size)
        val post1 = getListResult.posts[0]
        assertEquals("title", post1.title)
        assertEquals("<p>test content</p>", post1.content)

        val getRes = client.get("/api/admin/posts/${post1.id}")
        assertEquals(HttpStatusCode.OK, getRes.status)
        val getResult = objectMapper.readValue<GetPostResponseModel>(getRes.bodyAsText())

        assertEquals("title", getResult.title)
        assertEquals("<p>test content</p>", getResult.content)

        client.post("/api/admin/posts") {
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

        val res2 = client.get("/api/admin/posts")

        assertEquals(HttpStatusCode.OK, res2.status)
        val result2 = objectMapper.readValue<GetListPostResponseModel>(res2.bodyAsText())

        assertEquals(2, result2.posts.size)
        result2.posts[0].run {
            assertEquals("title2", this.title)
            assertEquals("<p>test content2</p>", this.content)
        }
        result2.posts[1].run {
            assertEquals("title", this.title)
            assertEquals("<p>test content</p>", this.content)
        }
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.get("/api/admin/posts") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }

        client.get("/api/admin/posts/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}