package com.itomise

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itomise.com.itomise.domain.auth.UserPrincipal
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.util.getKoinInstance
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class BaseTestApplication() {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        fun appTestApplication(
            block: suspend ApplicationTestBuilder.() -> Unit
        ) {
            testApplication {
                environment {
                    config = ApplicationConfig("application.test.conf")
                }
                application {
                    setUpTables()
                    routing {
                        post("/login-for-testing") {
                            val requestBody = call.receive<UserId>()
                            val createUserUseCase = getKoinInstance<ICreateUserUseCase>()
                            val result = createUserUseCase.handle(
                                ICreateUserUseCase.Command(
                                    name = "testtest",
                                    email = "testtest@example.com"
                                )
                            )

                            call.sessions.set(UserPrincipal(requestBody.toString()))
                            val sessionCookie = call.sessions.get<UserPrincipal>()
                            call.respond(
                                HttpStatusCode.OK,
                            )
                        }
                    }
                }

                block()
            }
        }

        suspend fun ApplicationTestBuilder.authSessionUser(
            userId: UUID
        ) {

        }

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

        fun cleanUp() {
            // test が fail すると Koin が残ったままになるため
            if (GlobalContext.getOrNull() != null) {
                stopKoin()
            }

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