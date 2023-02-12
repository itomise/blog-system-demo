package controller.post

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.controller.requestModels.CreatePostRequestModel
import com.itomise.admin.controller.responseModels.GetListPostResponseModel
import com.itomise.admin.domain.post.vo.PostStatus
import com.itomise.admin.util.removeHtmlTagFromString
import controller.BaseTestApplication.Companion.appTestApplication
import controller.BaseTestApplication.Companion.authSessionUserForTest
import controller.BaseTestApplication.Companion.cleanup
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

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ポストが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client)

        val test = removeHtmlTagFromString("<p>テストコンテンツ</p>")

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
                assertEquals(PostStatus.UN_PUBLISHED.value, it.status)
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