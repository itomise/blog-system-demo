package com.itomise.com.itomise.infrastructure.dao

import com.itomise.com.itomise.infrastructure.utils.StringIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime


object UserTable : StringIdTable(name = "users") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val name = varchar(name = "name", length = 255)
    val email = varchar(name = "email", length = 255)
}