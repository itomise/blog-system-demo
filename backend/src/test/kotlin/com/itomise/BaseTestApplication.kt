package com.itomise

import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import java.nio.file.Files
import java.nio.file.Path

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
                    setUpTables()
                }

                block()
            }
        }

        private fun setUpTables() {
            transaction {
                val migrationSql = getAllMigrationSql()
                val sql = """
                    create schema main;
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