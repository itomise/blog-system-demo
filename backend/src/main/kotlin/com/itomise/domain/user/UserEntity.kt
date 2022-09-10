package com.itomise.com.itomise.domain.user

import org.jetbrains.exposed.sql.Table

data class UserEntity(
    val id: UserId,
    val name: String
)

object UserTable: Table(name = "users") {
    val id = varchar(name = "id", length = 255)
    val name = varchar(name = "name", length = 255)

    override val primaryKey = PrimaryKey(id)
}