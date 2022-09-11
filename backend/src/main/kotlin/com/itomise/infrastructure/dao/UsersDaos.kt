package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserDao : Table(name = "users") {
    val createdAt = timestamp(name = "created_at").default(Instant.now())
    val updatedAt = timestamp(name = "updated_at").default(Instant.now())
    val id = varchar(name = "id", length = 255).index()
    val name = varchar(name = "name", length = 255)

    override val primaryKey = PrimaryKey(id)
}