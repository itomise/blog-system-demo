package com.itomise.com.itomise.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime


object UserLoginInfoTable : UUIDTable(name = "users_login_info", columnName = "user_id") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val email = varchar(name = "email", length = 100)
    val passwordHash = varchar(name = "password_hash", length = 255)
    val passwordSalt = varchar(name = "password_salt", length = 100)
    val hashAlgorithmId = integer(name = "hash_algorithm_id")
    val emailValidationStatus = integer(name = "email_validation_status")
    val confirmationToken = varchar(name = "confirmation_token", length = 100)
    val confirmationTokenExpires = datetime(name = "confirmation_token_expires")
    val passwordRecoveryToken = varchar(name = "password_recovery_token", length = 100)
    val passwordRecoveryTokenExpires = datetime(name = "password_recovery_token_expires")
}