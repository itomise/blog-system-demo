package controller.post

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.adminApi.controller.post.CreatePostRequestModel
import com.itomise.adminApi.controller.post.GetListPostResponseModel
import com.itomise.core.domain.post.vo.PostStatus
import com.itomise.core.util.removeHtmlTagFromString
import com.itomise.test.helper.KtorTestApplication.appTestApplication
import com.itomise.test.helper.KtorTestApplication.authSessionUserForTest
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CreatePostTest {
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ポストが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        val test = removeHtmlTagFromString("<p>テストコンテンツ</p>")

        client.post("/api/admin/posts") {
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

        client.get("/api/admin/posts").run {
            assertEquals(HttpStatusCode.OK, this.status)
            val result = objectMapper.readValue<GetListPostResponseModel>(this.bodyAsText())

            assertEquals(1, result.posts.size)
            result.posts.forEach {
                assertEquals("タイトル", it.title)
                assertEquals("<p>テストコンテンツ</p>", it.content)
                assertEquals(PostStatus.UN_PUBLISHED.value, it.status)
            }
        }
    }

    @Test
    fun `未ログインユーザーで叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.post("/api/admin/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    objectMapper.writeValueAsString(
                        CreatePostRequestModel(
                            title = "タイトル",
                            content = "<p>テストコンテンツ</p><p>testtest</p>"
                        )
                    )
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}