package com.itomise.com.itomise.domain.user

import org.jetbrains.exposed.sql.Table

data class UserEntity(
    val id: UserId,
    val name: String
)

object Users: Table() {
    val id = varchar("id", 255)
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)
}