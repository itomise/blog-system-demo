package com.itomise.com.itomise.infrastructure.repositories.account

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserHashAlgorithmId
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.account.vo.Username
import com.itomise.com.itomise.infrastructure.dao.UserLoginInfoTable
import com.itomise.com.itomise.infrastructure.dao.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository : IUserRepository {
    private fun resultRowToUserEntity(row: ResultRow): User {
        val t = row.getOrNull(UserLoginInfoTable.passwordHash)
        val user = User.from(
            id = UserId(row[UserTable.id].value),
            name = Username(row[UserTable.name]),
            email = Email(row[UserTable.email]),
            passwordHash = row.getOrNull(UserLoginInfoTable.passwordHash),
            passwordSalt = row.getOrNull(UserLoginInfoTable.passwordSalt),
            userHashAlgorithmId = if (row.getOrNull(UserLoginInfoTable.passwordSalt) != null) UserHashAlgorithmId.get(
                row[UserLoginInfoTable.hashAlgorithmId]
            ) else null,
        )
        return user
    }

    override suspend fun getList(): List<User> {
        return UserTable
            .join(
                otherTable = UserLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserLoginInfoTable.id }
            )
            .selectAll()
            .orderBy(UserTable.name)
            .map(::resultRowToUserEntity)
    }

    override suspend fun findByUserId(id: UserId): User? {
        return UserTable
            .join(
                otherTable = UserLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserLoginInfoTable.id }
            )
            .select(UserTable.id eq id.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    private suspend fun findLoginInfoByUserId(id: UserId): Boolean {
        val exists = UserLoginInfoTable
            .select(UserLoginInfoTable.id eq id.value)
            .firstOrNull()
        return exists != null
    }

    override suspend fun findByEmail(email: Email): User? {
        return UserTable
            .join(
                otherTable = UserLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserLoginInfoTable.id }
            )
            .select(UserTable.email eq email.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    override suspend fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            UserTable.update({
                UserTable.id eq user.id.value
            }) {
                it[name] = user.name.value
                it[email] = user.email.value
                it[updatedAt] = LocalDateTime.now()
            }
            user.loginInfo?.let {
                if (findLoginInfoByUserId(user.id)) {
                    UserLoginInfoTable.update({
                        UserLoginInfoTable.id eq user.id.value
                    }) {
                        it[passwordHash] = user.loginInfo.passwordHash
                        it[passwordSalt] = user.loginInfo.passwordSalt
                        it[hashAlgorithmId] = user.loginInfo.userHashAlgorithmId.value
                        it[updatedAt] = LocalDateTime.now()
                    }
                } else {
                    UserLoginInfoTable.insert {
                        it[id] = user.id.value
                        it[passwordHash] = user.loginInfo.passwordHash
                        it[passwordSalt] = user.loginInfo.passwordSalt
                        it[hashAlgorithmId] = user.loginInfo.userHashAlgorithmId.value
                    }
                }
            }
        } else {
            UserTable.insert {
                it[id] = user.id.value
                it[email] = user.email.value
                it[name] = user.name.value
            }
            user.loginInfo?.let {
                UserLoginInfoTable.insert {
                    it[id] = user.id.value
                    it[passwordHash] = user.loginInfo.passwordHash
                    it[passwordSalt] = user.loginInfo.passwordSalt
                    it[hashAlgorithmId] = user.loginInfo.userHashAlgorithmId.value
                }
            }
        }
    }

    override suspend fun delete(user: User) {
        UserTable.deleteWhere {
            UserTable.id eq user.id.value
        }
    }
}