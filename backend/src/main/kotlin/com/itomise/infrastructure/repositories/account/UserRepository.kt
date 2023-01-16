package com.itomise.com.itomise.infrastructure.repositories.account

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.infrastructure.dao.UserExternalLoginInfoTable
import com.itomise.com.itomise.infrastructure.dao.UserInternalLoginInfoTable
import com.itomise.com.itomise.infrastructure.dao.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository : IUserRepository {
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
            id = UserId(row[UserTable.id].value),
            email = Email(row[UserTable.email]),
            profile = profile,
            loginInfo = loginInfo,
        )
    }

    override suspend fun getList(): List<User> {
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

    override suspend fun findByUserId(id: UserId): User? {
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
            .select(UserTable.id eq id.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    private suspend fun findInternalLoginInfoByUserId(id: UserId): Boolean {
        val exists = UserInternalLoginInfoTable
            .select(UserInternalLoginInfoTable.id eq id.value)
            .firstOrNull()
        return exists != null
    }

    override suspend fun findByEmail(email: Email): User? {
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

    override suspend fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            update(user)
        } else {
            insert(user)
        }
    }

    private suspend fun update(user: User) {
        UserTable.update({
            UserTable.id eq user.id.value
        }) {
            it[name] = user.profile?.name?.value
            it[email] = user.email.value
            it[updatedAt] = LocalDateTime.now()
        }
        user.loginInfo?.let {
            if (user.loginInfo is UserInternalLoginInfo) {
                if (findInternalLoginInfoByUserId(user.id)) {
                    UserInternalLoginInfoTable.update({
                        UserInternalLoginInfoTable.id eq user.id.value
                    }) {
                        it[passwordHash] = user.loginInfo.passwordHash
                        it[passwordSalt] = user.loginInfo.passwordSalt
                        it[hashAlgorithmId] = user.loginInfo.hashAlgorithmId.value
                        it[updatedAt] = LocalDateTime.now()
                    }
                } else {
                    UserInternalLoginInfoTable.insert {
                        it[id] = user.id.value
                        it[passwordHash] = user.loginInfo.passwordHash
                        it[passwordSalt] = user.loginInfo.passwordSalt
                        it[hashAlgorithmId] = user.loginInfo.hashAlgorithmId.value
                    }
                }
            }
        }
    }

    private suspend fun insert(user: User) {
        UserTable.insert {
            it[id] = user.id.value
            it[email] = user.email.value
            it[name] = user.profile?.name?.value
        }
        user.loginInfo?.let {
            if (user.loginInfo is UserInternalLoginInfo) {
                UserInternalLoginInfoTable.insert {
                    it[id] = user.id.value
                    it[passwordHash] = user.loginInfo.passwordHash
                    it[passwordSalt] = user.loginInfo.passwordSalt
                    it[hashAlgorithmId] = user.loginInfo.hashAlgorithmId.value
                }
            }
            if (user.loginInfo is UserExternalLoginInfo) {
                UserExternalLoginInfoTable.insert {
                    it[id] = user.id.value
                    it[externalServiceType] = user.loginInfo.externalServiceType.value
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