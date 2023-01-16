package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime


object UserInternalLoginInfoTable : UUIDTable(name = "users_internal_login_info", columnName = "user_id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val passwordHash = varchar(name = "password_hash", length = 255)
    val passwordSalt = varchar(name = "password_salt", length = 100)
    val hashAlgorithmId = integer(name = "hash_algorithm_id")
}