package com.itomise.admin.infrastructure

import com.itomise.admin.module.envConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DataBaseFactory {
    private const val mainSchema = "main"
    fun getMainSchema() = mainSchema

    fun init() {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = envConfig.db.url
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        config.username = envConfig.db.user
        config.password = envConfig.db.password
        if (envConfig.db.instanceConnectionName != null && envConfig.db.instanceUnixSocket != null) {
            config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
            config.addDataSourceProperty("cloudSqlInstance", envConfig.db.instanceConnectionName)
            config.addDataSourceProperty("unixSocketPath", envConfig.db.instanceUnixSocket)
        }

        return HikariDataSource(config)
    }
}

suspend fun <T> dbQuery(block: suspend (transaction: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
    val mainSchema = DataBaseFactory.getMainSchema()
    SchemaUtils.setSchema(Schema(mainSchema))
    block(this)
}
