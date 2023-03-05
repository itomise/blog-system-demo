package com.itomise.test.helper

import com.itomise.blogDb.lib.DataBaseFactory
import io.mockk.every
import io.mockk.mockkObject
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object DatabaseTestHelper {

    fun mockNewSchema() {
        val schemaName = "test_${UUID.randomUUID().toString().replace("-", "")}"
        mockkObject(DataBaseFactory)
        every { DataBaseFactory.getMainSchema() } returns schemaName
    }

    fun setUpSchema() {
        val mainSchema = DataBaseFactory.getMainSchema()
        transaction {
            val sqlForDropAllSchema = """
                    drop schema if exists $mainSchema cascade;
                """.trimIndent()
            TransactionManager.current().exec(sqlForDropAllSchema)

            val migrationSql = getAllMigrationSql()
            val sql = """
                    create schema if not exists $mainSchema;
                    SET search_path TO $mainSchema;
                    $migrationSql
                """.trimIndent()

            TransactionManager.current().exec(sql)
        }
    }

    fun cleanupSchema() {
        val mainSchema = DataBaseFactory.getMainSchema()
        transaction {
            TransactionManager.current().exec("drop schema if exists $mainSchema cascade;")
        }
    }

    private fun getAllMigrationSql(): String {
        var sqlString = ""
        Files.list(Path.of("../blogDb/src/main/resources/db/migration"))
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