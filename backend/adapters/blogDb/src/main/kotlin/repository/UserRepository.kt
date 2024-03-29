package com.itomise.blogDb.repository

import com.itomise.blogDb.dao.UserExternalLoginInfoTable
import com.itomise.blogDb.dao.UserInternalLoginInfoTable
import com.itomise.blogDb.dao.UserTable
import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.vo.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository {
    private fun resultRowToUserEntity(row: ResultRow): User {
        val profile = row[UserTable.name]?.let {
            UserProfile(
                name = Username(it)
            )
        }

        val passwordHash = row.getOrNull(UserInternalLoginInfoTable.passwordHash)
        val passwordSalt = row.getOrNull(UserInternalLoginInfoTable.passwordSalt)
        val hashAlgorithmId = row.getOrNull(UserInternalLoginInfoTable.hashAlgorithmId)
        val externalLoginServiceType = row.getOrNull(UserExternalLoginInfoTable.externalServiceType)

        val loginInfo = if (passwordHash != null && passwordSalt != null && hashAlgorithmId != null) {
            UserInternalLoginInfo(
                passwordHash = passwordHash,
                passwordSalt = passwordSalt,
                hashAlgorithmId = UserHashAlgorithmId.get(hashAlgorithmId)
            )
        } else if (externalLoginServiceType != null) {
            UserExternalLoginInfo(
                externalServiceType = UserExternalLoginInfo.ExternalServiceType.get(externalLoginServiceType)
            )
        } else null

        return User.from(
            id = row[UserTable.id].value,
            email = Email(row[UserTable.email]),
            profile = profile,
            loginInfo = loginInfo,
        )
    }

    fun getList(): List<User> {
        return UserTable
            .join(
                otherTable = UserInternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserInternalLoginInfoTable.id }
            )
            .join(
                otherTable = UserExternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserExternalLoginInfoTable.id }
            )
            .selectAll()
            .orderBy(UserTable.email)
            .map(::resultRowToUserEntity)
    }

    fun findByUserId(id: UserId): User? {
        return UserTable
            .join(
                otherTable = UserInternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserInternalLoginInfoTable.id }
            )
            .join(
                otherTable = UserExternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserExternalLoginInfoTable.id }
            )
            .select(UserTable.id eq id)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    private fun findInternalLoginInfoByUserId(id: UserId): Boolean {
        val exists = UserInternalLoginInfoTable
            .select(UserInternalLoginInfoTable.id eq id)
            .firstOrNull()
        return exists != null
    }

    fun findByEmail(email: Email): User? {
        return UserTable
            .join(
                otherTable = UserInternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserInternalLoginInfoTable.id }
            )
            .join(
                otherTable = UserExternalLoginInfoTable,
                joinType = JoinType.LEFT,
                additionalConstraint = { UserTable.id eq UserExternalLoginInfoTable.id }
            )
            .select(UserTable.email eq email.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            update(user)
        } else {
            insert(user)
        }
    }

    private fun update(user: User) {
        UserTable.update({
            UserTable.id eq user.id
        }) {
            it[name] = user.profile?.name?.value
            it[email] = user.email.value
            it[updatedAt] = LocalDateTime.now()
        }
        user.loginInfo?.let { loginInfo ->
            if (loginInfo is UserInternalLoginInfo) {
                if (findInternalLoginInfoByUserId(user.id)) {
                    UserInternalLoginInfoTable.update({
                        UserInternalLoginInfoTable.id eq user.id
                    }) {
                        it[passwordHash] = loginInfo.passwordHash
                        it[passwordSalt] = loginInfo.passwordSalt
                        it[hashAlgorithmId] = loginInfo.hashAlgorithmId.value
                        it[updatedAt] = LocalDateTime.now()
                    }
                } else {
                    UserInternalLoginInfoTable.insert {
                        it[id] = user.id
                        it[passwordHash] = loginInfo.passwordHash
                        it[passwordSalt] = loginInfo.passwordSalt
                        it[hashAlgorithmId] = loginInfo.hashAlgorithmId.value
                    }
                }
            }
        }
    }

    private fun insert(user: User) {
        UserTable.insert {
            it[id] = user.id
            it[email] = user.email.value
            it[name] = user.profile?.name?.value
        }
        user.loginInfo?.let { loginInfo ->
            if (loginInfo is UserInternalLoginInfo) {
                UserInternalLoginInfoTable.insert {
                    it[id] = user.id
                    it[passwordHash] = loginInfo.passwordHash
                    it[passwordSalt] = loginInfo.passwordSalt
                    it[hashAlgorithmId] = loginInfo.hashAlgorithmId.value
                }
            }
            if (loginInfo is UserExternalLoginInfo) {
                UserExternalLoginInfoTable.insert {
                    it[id] = user.id
                    it[externalServiceType] = loginInfo.externalServiceType.value
                }
            }
        }
    }

    fun delete(user: User) {
        UserTable.deleteWhere {
            UserTable.id eq user.id
        }
    }
}