package com.itomise.com.itomise.infrastructure.dao

import com.itomise.com.itomise.infrastructure.utils.StringEntityClass
import com.itomise.com.itomise.infrastructure.utils.StringIdTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserDao : StringIdTable(name = "users") {
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime())
    val name = varchar(name = "name", length = 255)
}

class UserDaoEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : StringEntityClass<UserDaoEntity>(UserDao)

    var name by UserDao.name
    var createdAt by UserDao.createdAt
    var updatedAt by UserDao.updatedAt
}