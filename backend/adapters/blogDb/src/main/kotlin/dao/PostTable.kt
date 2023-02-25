package com.itomise.blogDb.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PostTable : UUIDTable(name = "post", columnName = "id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val title = varchar(name = "title", length = 255)
    val content = text(name = "content")
    val status = integer(name = "status")
    val publishedAt = datetime(name = "published_at").nullable()
}