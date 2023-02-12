package helper

import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path

object DatabaseTestHelper {
    fun setUpSchema() {
        transaction {
            val sqlForDropAllSchema = """
                    drop schema if exists main cascade;
                """.trimIndent()
            TransactionManager.current().exec(sqlForDropAllSchema)

            val migrationSql = getAllMigrationSql()
            val sql = """
                    create schema if not exists main;
                    SET search_path TO main;
                    $migrationSql
                """.trimIndent()

            TransactionManager.current().exec(sql)
        }
    }

    fun cleanupSchema() {
        transaction {
            val sqlForDropAllSchema = """
                    drop schema if exists main cascade;
                """.trimIndent()
            TransactionManager.current().exec(sqlForDropAllSchema)
        }
    }

    private fun getAllMigrationSql(): String {
        var sqlString = ""
        Files.list(Path.of("../db/migration"))
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