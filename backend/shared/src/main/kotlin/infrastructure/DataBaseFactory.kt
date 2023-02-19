package com.itomise.shared.infrastructure

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

    fun init(
        url: String,
        user: String,
        password: String,
        instanceConnectionName: String? = null,
        instanceUnixSocket: String? = null
    ) {
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
        if (instanceConnectionName != null && instanceUnixSocket != null) {
            config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
            config.addDataSourceProperty("cloudSqlInstance", instanceConnectionName)
            config.addDataSourceProperty("unixSocketPath", instanceUnixSocket)
        }

        Database.connect(HikariDataSource(config))
    }
}

suspend fun <T> dbQuery(block: suspend (transaction: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
    val mainSchema = DataBaseFactory.getMainSchema()
    SchemaUtils.setSchema(Schema(mainSchema))
    block(this)
}
