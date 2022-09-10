package com.itomise.com.itomise.utils.dao

import com.itomise.com.itomise.domain.user.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseFactory {
    fun init(url: String, user: String, password: String) {
        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }
}

fun <T> dbQuery(block: () -> T): T = transaction {
    SchemaUtils.setSchema(Schema("main"))
    block()
}