package com.itomise.infrastructure

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DataBaseFactory {
    fun init(url: String, user: String, password: String) {
        Database.connect(
            hikari(
                url = url,
                user = user,
                password = password
            )
        )
    }

    private fun hikari(url: String, user: String, password: String): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        config.username = user
        config.password = password

        return HikariDataSource(config)
    }
}

suspend fun <T> dbQuery(block: suspend (transaction: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
    SchemaUtils.setSchema(Schema("main"))
    block(this)
}
