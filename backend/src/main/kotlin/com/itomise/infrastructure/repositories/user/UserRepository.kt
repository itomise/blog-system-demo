package com.itomise.com.itomise.infrastructure.repositories.user

import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.Email
import com.itomise.com.itomise.infrastructure.dao.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.util.*

class UserRepository : IUserRepository {
    private fun resultRowToUserEntity(row: ResultRow) = User.create(
        id = row[UserTable.id].value,
        name = row[UserTable.name],
        email = Email(row[UserTable.email])
    )

    override suspend fun getList(): List<User> {
        return UserTable
            .slice(UserTable.id, UserTable.email, UserTable.name)
            .selectAll()
            .orderBy(UserTable.name)
            .map(::resultRowToUserEntity)
    }

    override suspend fun findByUserId(id: UUID): User? {
        return UserTable
            .slice(UserTable.id, UserTable.email, UserTable.name)
            .select(UserTable.id eq id)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    override suspend fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            UserTable.update({
                UserTable.id eq user.id
            }) {
                it[name] = user.name
                it[email] = user.email.value
                it[updatedAt] = LocalDateTime.now()
            }
        } else {
            UserTable.insert {
                it[id] = user.id
                it[email] = user.email.value
                it[name] = user.name
            }
        }
    }

    override suspend fun delete(user: User) {
        UserTable.deleteWhere {
            UserTable.id eq user.id
        }
    }
}