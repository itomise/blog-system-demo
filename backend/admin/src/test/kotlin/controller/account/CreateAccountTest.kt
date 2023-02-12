package controller.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.controller.requestModels.CreateUserRequestModel
import com.itomise.admin.controller.responseModels.CreateUserResponseModel
import com.itomise.admin.controller.responseModels.GetListUserResponseModel
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.auth.IActivateUserUseCase
import controller.BaseTestApplication.Companion.appTestApplication
import controller.BaseTestApplication.Companion.authSessionUserForTest
import controller.BaseTestApplication.Companion.cleanup
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CreateAccountTest : KoinComponent {
    val createUserUseCase by inject<ICreateAccountUseCase>()
    val useService by inject<IUserService>()
    val activateUserCase by inject<IActivateUserUseCase>()

    @AfterTest
    fun after() = cleanup()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ユーザーが作成できること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }

        authSessionUserForTest(client, "05テスト太郎", "05@example.com")

        val res = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("test@test.test")))
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
        val resBody = objectMapper.readValue<CreateUserResponseModel>(res.bodyAsText())

        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, this.status)

            val res = objectMapper.readValue<GetListUserResponseModel>(this.bodyAsText())
            assertEquals(2, res.users.size)
            res.users.find { it.id == resBody.id }?.run {
                assertEquals(resBody.id, this.id)
                assertEquals(null, this.name)
                assertEquals("test@test.test", this.email)
            }
        }
    }

    @Test
    fun `未ログイン状態で叩くと401になること`() = appTestApplication {
        val client = createClient { install(HttpCookies) }
        client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsString(CreateUserRequestModel("04@example.com")))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, this.status)
        }
    }
}