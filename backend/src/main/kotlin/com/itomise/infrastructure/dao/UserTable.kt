package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime


object UserTable : UUIDTable(name = "users", columnName = "id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val name = varchar(name = "name", length = 50)
    val email = varchar(name = "email", length = 100)
}