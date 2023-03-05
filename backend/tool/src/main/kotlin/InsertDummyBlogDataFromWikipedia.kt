import com.github.kittinunf.fuel.Fuel
import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.PostRepository
import com.itomise.core.domain.post.entities.Post
import com.itomise.core.lib.fuel.responseCustomObject
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jsoup.Jsoup

fun main(): Unit = runBlocking {
    // Wikipedia APIのURLを生成する
    val urlBuilder = URLBuilder("https://ja.wikipedia.org/w/api.php")
    urlBuilder.parameters.apply {
        append("action", "query")
        append("format", "json")
        append("list", "random")
        append("rnlimit", "500")
        append("rnnamespace", "0")
    }

    // Wikipedia APIからランダムに記事を取得する
    val (_, _, result) = Fuel.get(urlBuilder.buildString())
        .header("Content-Type", "application/json; charset=utf-8")
        .responseCustomObject<WikipediaJson>()

    val wikipediaJson = result.get()

    // PostgreSQLに接続する
    Database.connect(
        url = "jdbc:postgresql://localhost:15432/local",
        driver = "org.postgresql.Driver",
        user = "root",
        password = "root"
    )

    val postRepository = PostRepository()

    // ニュース記事を1つずつ処理する
    dbQuery {
        wikipediaJson.query.random.forEach {
            // 記事のページを取得する
            val articleUrl = "https://ja.wikipedia.org/wiki/${it.title}"

            val (_, _, articleResult) = Fuel.get(articleUrl).responseString()

            // Jsoupを使用してHTMLを解析する
            val article = Jsoup.parse(articleResult.get())

            // タイトルと本文を取得する
            val articleTitle = article.selectFirst("#firstHeading")!!.text()
            val articleBody =
                article.selectFirst("#bodyContent")!!.text()
                    .replace("出典: フリー百科事典『ウィキペディア（Wikipedia）』", "")
                    .trim()

            val post = Post.new(
                title = articleTitle,
                content = "<p>$articleBody</p>"
            ).publish()

            postRepository.save(post)
        }
    }
}

data class WikipediaJson(
    val query: WikipediaJsonQuery
)

data class WikipediaJsonQuery(
    val random: List<WikipediaJsonQueryRandom>
)

data class WikipediaJsonQueryRandom(
    val id: Int,
    val ns: Int,
    val title: String
)