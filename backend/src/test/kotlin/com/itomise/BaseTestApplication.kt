package com.itomise

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.account.vo.UserPrincipal
import com.itomise.com.itomise.domain.account.vo.Username
import com.itomise.com.itomise.lib.sendgrid.SendGridClient
import com.itomise.com.itomise.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
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
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class BaseTestApplication() {
    companion object {
        fun appTestApplication(
            block: suspend ApplicationTestBuilder.() -> Unit
        ) {
            testApplication {
                environment {
                    config = ApplicationConfig("application.test.conf")
                }
                application {
                    // application が起動してからでないと DB などが立ち上がっていないため
                    setUpTables()

                    // メールは全てモックをさす
                    mockkObject(SendGridClient)
                    every { SendGridClient.send(any()) } answers {}

                    routing {
                        post("/login-for-testing") {
                            val request = call.receive<CreateTestUserRequest>()
                            val createUserUseCase = getKoinInstance<ICreateAccountUseCase>()
                            val userService = getKoinInstance<IUserService>()
                            val activateUserUseCase = getKoinInstance<IActivateUserUseCase>()

                            val result = createUserUseCase.handle(
                                ICreateAccountUseCase.Command(
                                    name = request.name,
                                    email = request.email,
                                )
                            )

                            activateUserUseCase.handle(
                                IActivateUserUseCase.Command(
                                    token = userService.generateActivationToken(
                                        User.from(
                                            id = UserId(result),
                                            name = Username(request.name),
                                            email = Email(request.email),
                                            passwordSalt = null,
                                            passwordHash = null,
                                            userHashAlgorithmId = null
                                        )
                                    ),
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
                                            name = Username(request.name),
                                            email = Email(request.email),
                                            passwordSalt = null,
                                            passwordHash = null,
                                            userHashAlgorithmId = null
                                        )
                                    ),
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

        private fun setUpTables() {
            transaction {
                val migrationSql = getAllMigrationSql()
                val sql = """
                    create schema if not exists main;
                    SET search_path TO main;
                    $migrationSql
                """.trimIndent()

                TransactionManager.current().exec(sql)
            }
        }

        fun cleanup() {
            // test が fail すると Koin が残ったままになるため
            if (GlobalContext.getOrNull() != null) {
                stopKoin()
            }

            unmockkAll()

            transaction {
                val sqlForDropAllSchema = """
                    drop schema main cascade;
                """.trimIndent()
                TransactionManager.current().exec(sqlForDropAllSchema)
            }
        }

        private fun getAllMigrationSql(): String {
            var sqlString = ""
            Files.list(Path.of("./db/migration"))
                .filter { it.toString().endsWith(".sql") }
                .sorted(compareBy {
                    val versionNum = it.toString().split("__")[0]
                    versionNum.split("V")[1].toInt()
                })
                .forEach { path ->
                    Files.readAllLines(path)
                        .filter { it.toString().isNotBlank() }
                        .forEach {
                            val str = it.toString()
                            sqlString += " $str"
                        }
                }
            return sqlString
        }
    }
}