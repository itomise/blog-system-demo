package com.itomise.com.itomise.infrastructure.repositories.auth

import com.itomise.com.itomise.domain.auth.EmailValidationStatus
import com.itomise.com.itomise.domain.auth.HashAlgorithm
import com.itomise.com.itomise.domain.auth.UserLoginInfo
import com.itomise.com.itomise.domain.auth.interfaces.IUserLoginInfoRepository
import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.infrastructure.dao.UserLoginInfoTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.*

class UserLoginInfoRepository : IUserLoginInfoRepository {
    private fun resultRowToUserLoginInfoEntity(row: ResultRow): UserLoginInfo = UserLoginInfo.from(
        userId = row[UserLoginInfoTable.id].value,
        email = Email(row[UserLoginInfoTable.email]),
        passwordHash = row[UserLoginInfoTable.passwordHash],
        passwordSalt = row[UserLoginInfoTable.passwordSalt],
        hashAlgorithmId = HashAlgorithm.get(row[UserLoginInfoTable.hashAlgorithmId]),
        emailValidationStatus = EmailValidationStatus.get(row[UserLoginInfoTable.emailValidationStatus]),
        confirmationToken = row[UserLoginInfoTable.confirmationToken],
        confirmationTokenExpires = row[UserLoginInfoTable.confirmationTokenExpires],
        passwordRecoveryToken = row[UserLoginInfoTable.passwordRecoveryToken],
        passwordRecoveryTokenExpires = row[UserLoginInfoTable.passwordRecoveryTokenExpires]
    )

    override suspend fun findByUserId(userId: UUID): UserLoginInfo? {
        return UserLoginInfoTable
            .select(UserLoginInfoTable.id eq userId)
            .map(::resultRowToUserLoginInfoEntity)
            .firstOrNull()
    }

    override suspend fun findByEmail(email: Email): UserLoginInfo? {
        return UserLoginInfoTable
            .select(UserLoginInfoTable.email eq email.value)
            .map(::resultRowToUserLoginInfoEntity)
            .firstOrNull()
    }

    override suspend fun save(userLoginInfo: UserLoginInfo) {
        UserLoginInfoTable.insert {
            it[id] = userLoginInfo.userId
            it[email] = userLoginInfo.email.value
            it[passwordHash] = userLoginInfo.passwordHash
            it[passwordSalt] = userLoginInfo.passwordSalt
            it[hashAlgorithmId] = userLoginInfo.hashAlgorithmId.id
            it[emailValidationStatus] = userLoginInfo.emailValidationStatus.value
//            it[confirmationToken] = userLoginInfo.confirmationToken
//            it[confirmationTokenExpires] = userLoginInfo.confirmationTokenExpires
//            it[passwordRecoveryToken] = userLoginInfo.passwordRecoveryToken
//            it[passwordRecoveryTokenExpires] = userLoginInfo.passwordRecoveryTokenExpires
        }
    }
}