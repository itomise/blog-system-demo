package helper

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.domain.account.vo.UserPrincipal
import com.itomise.admin.lib.sendgrid.SendGridClient
import com.itomise.admin.module.jwkProvider
import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.shared.infrastructure.DataBaseFactory
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.util.*

object KtorTestApplication {
    fun appTestApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) {
        testApplication {
            environment {
                config = ApplicationConfig("application.test.conf")
            }
            application {
                val schemaName = "test${UUID.randomUUID().toString().replace("-", "")}"
                mockkObject(DataBaseFactory)
                every { DataBaseFactory.getMainSchema() } returns schemaName

                // application が起動してからでないと DB などが立ち上がっていないため
                DatabaseTestHelper.setUpSchema()

                // メールは全てモックをさす
                mockkObject(SendGridClient)
                every { SendGridClient.send(any()) } answers {}

                val mockJwkProvider = mockk<JwkProvider>()
                every { mockJwkProvider.get(any()) } returns Jwk.fromValues(
                    mapOf(
                        "use" to "sig",
                        "kty" to "RSA",
                        "n" to "ql5HLRkh27pA0zONngdyk_mRo7TgcR7Et31YbqDsWDcfLYN_P1XrdTetg89HHuSC_P_G5rabx3NIzenK_Ej8lZES0F7lpagIwaMZQjPO0urEp53MuRhoogppBr6uxRP_Mkv-bESK_cTNaTgG5nfkWzjfq6Bx1wjNOhWQDONAd81V9jFt8vm0oDzdErsBKUmuysRo7Seuol2mgD5D8AyBYyv79NRaP1anuje4h0DUo45yevxOjjdyAwCcM6uZPPNtDN7AoM469il--rgV-17DJSz3lKnYUdwepqn0ULeqCPORRjJ0p-B5VDJI0_CuYMiO8XsQh43y-znq8pYiIkISow",
                        "e" to "AQAB",
                        "kid" to "673a5ea3-a4ae-422b-be55-a9c98e674550",
                        "alg" to "RS256"
                    )
                )

                jwkProvider = mockJwkProvider

                routing {
                    post("/login-for-testing") {
                        val request = call.receive<CreateTestUserRequest>()
                        val createUserUseCase = getKoinInstance<ICreateAccountUseCase>()
                        val userService = getKoinInstance<IUserService>()
                        val activateUserUseCase = getKoinInstance<IActivateUserUseCase>()

                        val result = createUserUseCase.handle(ICreateAccountUseCase.Command(request.email))

                        activateUserUseCase.handle(
                            IActivateUserUseCase.Command(
                                token = userService.generateActivationToken(
                                    User.from(
                                        id = UserId(result),
                                        email = Email(request.email),
                                        profile = null,
                                        loginInfo = null
                                    )
                                ),
                                name = request.name,
                                password = request.password
                            )
                        )


                        call.sessions.set(UserPrincipal(result.toString()))
                        call.respond(
                            HttpStatusCode.OK, TestUser(
                                id = result,
                                name = request.name,
                                email = request.email,
                                password = request.password,
                            )
                        )
                    }
                    post("/activate-for-testing") {
                        val request = call.receive<ActivateTestUserRequest>()
                        val userService = getKoinInstance<IUserService>()
                        val activateUserUseCase = getKoinInstance<IActivateUserUseCase>()

                        activateUserUseCase.handle(
                            IActivateUserUseCase.Command(
                                token = userService.generateActivationToken(
                                    User.from(
                                        id = UserId(request.id),
                                        email = Email(request.email),
                                        profile = null,
                                        loginInfo = null
                                    )
                                ),
                                name = request.name,
                                password = request.password
                            )
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            block()
        }
    }

    suspend fun authSessionUserForTest(
        client: HttpClient,
        name: String = "テスト太郎",
        email: String = "${UUID.randomUUID()}@example.com",
        password: String = "${UUID.randomUUID()}"
    ): TestUser {
        val objectMapper = jacksonObjectMapper()
        val loginRes = client.post("/login-for-testing") {
            contentType(ContentType.Application.Json)
            setBody(
                objectMapper.writeValueAsString(
                    CreateTestUserRequest(
                        name = name,
                        email = email,
                        password = password
                    )
                )
            )
        }
        return objectMapper.readValue(loginRes.bodyAsText())
    }

    private data class CreateTestUserRequest(
        val name: String,
        val email: String,
        val password: String
    )

    data class ActivateTestUserRequest(
        val id: UUID,
        val name: String,
        val email: String,
        val password: String
    )

    data class TestUser(
        val id: UUID,
        val name: String,
        val email: String,
        val password: String
    )

    fun cleanup() {
        // test が fail すると Koin が残ったままになるため
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }

        unmockkAll()

        DatabaseTestHelper.cleanupSchema()
    }
}

private inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}