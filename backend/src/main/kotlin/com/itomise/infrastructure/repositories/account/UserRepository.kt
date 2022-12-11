package com.itomise.com.itomise.infrastructure.repositories.account

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.infrastructure.dao.UserLoginInfoTable
import com.itomise.com.itomise.infrastructure.dao.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository : IUserRepository {
    private fun resultRowToUserEntity(row: ResultRow) = User.from(
        id = UserId(row[UserTable.id].value),
        name = Username(row[UserTable.name]),
        email = Email(row[UserTable.email]),
        passwordHash = row[UserLoginInfoTable.passwordHash],
        passwordSalt = row[UserLoginInfoTable.passwordSalt],
        userHashAlgorithmId = UserHashAlgorithmId.get(row[UserLoginInfoTable.hashAlgorithmId]),
        emailValidationStatus = EmailValidationStatus.get(row[UserLoginInfoTable.emailValidationStatus]),
        confirmationToken = row[UserLoginInfoTable.confirmationToken],
        confirmationTokenExpires = row[UserLoginInfoTable.confirmationTokenExpires],
        passwordRecoveryToken = row[UserLoginInfoTable.passwordRecoveryToken],
        passwordRecoveryTokenExpires = row[UserLoginInfoTable.passwordRecoveryTokenExpires]
    )

    override suspend fun getList(): List<User> {
        return UserTable
            .join(
                otherTable = UserLoginInfoTable,
                joinType = JoinType.INNER,
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
                joinType = JoinType.INNER,
                additionalConstraint = { UserTable.id eq UserLoginInfoTable.id }
            )
            .select(UserTable.id eq id.value)
            .map(::resultRowToUserEntity)
            .firstOrNull()
    }

    override suspend fun findByEmail(email: Email): User? {
        return UserTable
            .join(
                otherTable = UserLoginInfoTable,
                joinType = JoinType.INNER,
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
            UserLoginInfoTable.update({
                UserLoginInfoTable.id eq user.id.value
            }) {
                it[email] = user.email.value
                it[passwordHash] = user.loginInfo.passwordHash
                it[passwordSalt] = user.loginInfo.passwordSalt
                it[hashAlgorithmId] = user.loginInfo.userHashAlgorithmId.value
                it[emailValidationStatus] = user.loginInfo.emailValidationStatus.value
                it[updatedAt] = LocalDateTime.now()
                user.loginInfo.confirmationToken?.let { confirmationToken ->
                    it[UserLoginInfoTable.confirmationToken] = confirmationToken
                }
                user.loginInfo.confirmationTokenExpires?.let { confirmationTokenExpires ->
                    it[UserLoginInfoTable.confirmationTokenExpires] = confirmationTokenExpires
                }
                user.loginInfo.passwordRecoveryToken?.let { passwordRecoveryToken ->
                    it[UserLoginInfoTable.passwordRecoveryToken] = passwordRecoveryToken
                }
                user.loginInfo.passwordRecoveryTokenExpires?.let { passwordRecoveryTokenExpires ->
                    it[UserLoginInfoTable.passwordRecoveryTokenExpires] = passwordRecoveryTokenExpires
                }
            }
        } else {
            UserTable.insert {
                it[id] = user.id.value
                it[email] = user.email.value
                it[name] = user.name.value
            }
            UserLoginInfoTable.insert {
                it[id] = user.id.value
                it[email] = user.email.value
                it[passwordHash] = user.loginInfo.passwordHash
                it[passwordSalt] = user.loginInfo.passwordSalt
                it[hashAlgorithmId] = user.loginInfo.userHashAlgorithmId.value
                it[emailValidationStatus] = user.loginInfo.emailValidationStatus.value
                user.loginInfo.confirmationToken?.let { confirmationToken ->
                    it[UserLoginInfoTable.confirmationToken] = confirmationToken
                }
                user.loginInfo.confirmationTokenExpires?.let { confirmationTokenExpires ->
                    it[UserLoginInfoTable.confirmationTokenExpires] = confirmationTokenExpires
                }
                user.loginInfo.passwordRecoveryToken?.let { passwordRecoveryToken ->
                    it[UserLoginInfoTable.passwordRecoveryToken] = passwordRecoveryToken
                }
                user.loginInfo.passwordRecoveryTokenExpires?.let { passwordRecoveryTokenExpires ->
                    it[UserLoginInfoTable.passwordRecoveryTokenExpires] = passwordRecoveryTokenExpires
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