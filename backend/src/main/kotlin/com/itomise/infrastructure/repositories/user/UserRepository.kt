package com.itomise.com.itomise.infrastructure.repositories.user

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.domain.user.vo.UserId
import com.itomise.com.itomise.infrastructure.dao.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository : IUserRepository {
    private fun resultRowToUserEntity(row: ResultRow) = User.create(
        id = UserId(row[UserTable.id].value),
        name = row[UserTable.name],
        email = Email(row[UserTable.email])
    )

    override suspend fun getList(): List<User> {
        return UserTable
            .slice(UserTable.id, UserTable.name, UserTable.email)
            .selectAll()
            .orderBy(UserTable.name)
            .map(::resultRowToUserEntity)
    }

    override suspend fun findByUserId(id: UserId): User? {
        return UserTable
            .slice(UserTable.id, UserTable.name, UserTable.email)
            .select(UserTable.id eq id.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    override suspend fun findByEmail(email: Email): User? {
        return UserTable
            .slice(UserTable.id, UserTable.name, UserTable.email)
            .select(UserTable.email eq email.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    override suspend fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            UserTable.update {
                it[name] = user.name
                it[updatedAt] = LocalDateTime.now()
            }
        } else {
            UserTable.insert {
                it[id] = user.id.value
                it[name] = user.name
                it[email] = user.email.value
            }
        }
    }

    override suspend fun delete(user: User) {
        UserTable.deleteWhere {
            UserTable.id eq user.id.value
        }
    }
}