package com.itomise.blogDb.dao

import com.itomise.blogDb.util.StringIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object SessionTable : StringIdTable(name = "session", columnName = "id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val userId = uuid(name = "user_id")
    val expireAt = datetime(name = "expire_at")
}