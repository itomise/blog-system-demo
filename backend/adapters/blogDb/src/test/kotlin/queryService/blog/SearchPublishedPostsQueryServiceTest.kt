package queryService.blog

import com.itomise.blogDb.lib.DataBaseFactory
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.queryService.blog.SearchPublishedPostsQueryService
import com.itomise.blogDb.repository.PostRepository
import com.itomise.core.domain.post.entities.Post
import com.itomise.test.helper.DatabaseTestHelper
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchPublishedPostsQueryServiceTest {
    private val searchPublishedPostsQueryService = SearchPublishedPostsQueryService()
    private val postRepository = PostRepository()

    @BeforeTest
    fun before() {
        DataBaseFactory.init(
            url = System.getenv("DB_URL"),
            user = System.getenv("DB_USER"),
            password = System.getenv("DB_PASSWORD")
        )

        DatabaseTestHelper.mockNewSchema()
        DatabaseTestHelper.setUpSchema()

        runBlocking {
            dbQuery {
                (1..100).forEach {
                    postRepository.save(
                        Post.new(
                            title = "テストタイトル$it",
                            content = "<p>テストコンテンツ$it</p>"
                        ).publish()
                    )
                }
            }
        }
    }

    @AfterTest
    fun cleanup() {
        DatabaseTestHelper.cleanupSchema()
    }

    @Test
    fun `部分一致で検索できること`() = runBlocking {
        val result = dbQuery {
            it.addLogger(StdOutSqlLogger)
            searchPublishedPostsQueryService.handle(
                limit = 10,
                offset = 0,
                query = "コンテンツ"
            )
        }

        assertEquals(10, result.size)
    }

    @Test
    fun `limitが正しく動作していること`() = runBlocking {
        val result = dbQuery {
            it.addLogger(StdOutSqlLogger)
            searchPublishedPostsQueryService.handle(
                limit = 20,
                offset = 0,
                query = "コンテンツ"
            )
        }

        assertEquals(20, result.size)
    }

    @Test
    fun `offsetが正しく動作していること`() = runBlocking {
        val result = dbQuery {
            it.addLogger(StdOutSqlLogger)
            searchPublishedPostsQueryService.handle(
                limit = 10,
                offset = 99,
                query = "コンテンツ"
            )
        }

        assertEquals(1, result.size)
    }

    @Test
    fun `クエリにマッチしない場合は空の配列が返ること`() = runBlocking {
        val result = dbQuery {
            it.addLogger(StdOutSqlLogger)
            searchPublishedPostsQueryService.handle(
                limit = 10,
                offset = 0,
                query = "ほげほげ"
            )
        }

        assertEquals(0, result.size)
    }
}