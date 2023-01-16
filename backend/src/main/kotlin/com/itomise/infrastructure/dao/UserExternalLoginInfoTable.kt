package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime


object UserExternalLoginInfoTable : UUIDTable(name = "users_external_login_info", columnName = "user_id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val externalServiceType = integer(name = "external_service_type")
}