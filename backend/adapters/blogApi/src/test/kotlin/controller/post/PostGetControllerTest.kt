package controller.post

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.blogApi.controller.post.PostGetListResponseModel
import com.itomise.blogDb.repository.PostRepository
import com.itomise.core.domain.post.entities.Post
import helper.BlogApiTestApplication.appTestApplication
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.component.inject
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals


class PostGetControllerTest : KoinTest {
    private val objectMapper = jacksonObjectMapper()
    private val postRepository by inject<PostRepository>()

    @Test
    fun `公開されているポストが取得できること`() = appTestApplication {
        (0..2).forEach {
            val post = Post.new(title = "テストタイトル$it", content = "<p>テストコンテンツ$it</p>").publish()
            postRepository.save(post)
        }

        val sut = client.get("/api/public/posts")

        assertEquals(HttpStatusCode.OK, sut.status)
        val posts = objectMapper.readValue<PostGetListResponseModel>(sut.bodyAsText()).posts
        assertEquals(3, posts.size)
        posts.forEachIndexed { index, post ->
            when (index) {
                0 -> {
                    assertEquals("テストタイトル1", post.title)
                    assertEquals("<p>テストコンテンツ1</p>", post.content)
                    assertEquals("テストコンテンツ1", post.displayContent)
                    assert(post.publishedAt != null)
                }

                1 -> {
                    assertEquals("テストタイトル2", post.title)
                    assertEquals("<p>テストコンテンツ2</p>", post.content)
                    assertEquals("テストコンテンツ2", post.displayContent)
                    assert(post.publishedAt != null)
                }

                2 -> {
                    assertEquals("テストタイトル3", post.title)
                    assertEquals("<p>テストコンテンツ3</p>", post.content)
                    assertEquals("テストコンテンツ3", post.displayContent)
                    assert(post.publishedAt != null)
                }
            }
        }
    }

    @Test
    fun `公開されていないポストを取得できないこと`() = appTestApplication {
        (0..2).forEach {
            val post = Post.new(title = "テストタイトル$it", content = "<p>テストコンテンツ$it</p>")
            postRepository.save(post)
        }

        val sut = client.get("/api/public/posts")

        assertEquals(HttpStatusCode.OK, sut.status)
        val posts = objectMapper.readValue<PostGetListResponseModel>(sut.bodyAsText()).posts
        assertEquals(0, posts.size)
    }
}