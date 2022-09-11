package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserDao : Table(name = "users") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val id = varchar(name = "id", length = 255).index()
    val name = varchar(name = "name", length = 255)

    override val primaryKey = PrimaryKey(id)
}