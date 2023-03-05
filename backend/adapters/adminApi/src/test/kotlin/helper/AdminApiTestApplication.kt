package helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.core.domain.user.vo.UserPrincipal
import com.itomise.test.factory.UserFactory
import com.itomise.test.helper.DatabaseTestHelper
import com.itomise.test.mock.SendGridClientMock
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
import io.mockk.unmockkAll
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.util.*

object AdminApiTestApplication {
    fun appTestApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) {
        // test が fail すると Koin が残ったままになるため
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        unmockkAll()

        testApplication {
            environment {
                config = ApplicationConfig("application.test.conf")
            }
            application {
                DatabaseTestHelper.mockNewSchema()

                // application が起動してからでないと DB などが立ち上がっていないため
                DatabaseTestHelper.setUpSchema()

                SendGridClientMock.execute()

                JwkProviderMock.handle()

                routing {
                    post("/login-for-testing") {
                        val request = call.receive<CreateTestUserRequest>()

                        val user = UserFactory.create(request.name, request.email, request.password)

                        call.sessions.set(UserPrincipal(user.id.toString()))
                        call.respond(
                            HttpStatusCode.OK, TestUser(
                                id = user.id,
                                name = request.name,
                                email = request.email,
                                password = request.password,
                            )
                        )
                    }
                    post("/activate-for-testing") {
                        val request = call.receive<ActivateTestUserRequest>()

                        UserFactory.activate(
                            request.id,
                            request.name,
                            request.password
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
}