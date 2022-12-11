package com.itomise.com.itomise.domain.auth

import com.itomise.com.itomise.domain.user.vo.Email
import java.time.LocalDateTime
import java.util.*

class UserLoginInfo private constructor(
    val userId: UUID,
    val email: Email,
    val passwordHash: String,
    val passwordSalt: String,
    val hashAlgorithmId: HashAlgorithm,
    val emailValidationStatus: EmailValidationStatus,
    val confirmationToken: String?,
    val confirmationTokenExpires: LocalDateTime?,
    val passwordRecoveryToken: String?,
    val passwordRecoveryTokenExpires: LocalDateTime?,
) {
    companion object {
        fun from(
            userId: UUID,
            email: Email,
            passwordHash: String,
            passwordSalt: String,
            hashAlgorithmId: HashAlgorithm,
            emailValidationStatus: EmailValidationStatus,
            confirmationToken: String? = null,
            confirmationTokenExpires: LocalDateTime? = null,
            passwordRecoveryToken: String? = null,
            passwordRecoveryTokenExpires: LocalDateTime? = null,
        ) = UserLoginInfo(
            userId = userId,
            email = email,
            passwordHash = passwordHash,
            passwordSalt = passwordSalt,
            hashAlgorithmId = hashAlgorithmId,
            emailValidationStatus = emailValidationStatus,
            confirmationToken = confirmationToken,
            confirmationTokenExpires = confirmationTokenExpires,
            passwordRecoveryToken = passwordRecoveryToken,
            passwordRecoveryTokenExpires = passwordRecoveryTokenExpires,
        )

        fun generate(
            userId: UUID,
            email: Email,
            password: String,
        ): UserLoginInfo {
            val hashAlgorithmId = HashAlgorithm.getRandom()
            val passwordSalt = UUID.randomUUID().toString()
            val passwordHash = UserLoginInfoService.generatePasswordHash(
                password = password,
                salt = passwordSalt,
                hashAlgorithm = hashAlgorithmId
            )

            return UserLoginInfo(
                userId = userId,
                email = email,
                passwordHash = passwordHash,
                passwordSalt = passwordSalt,
                hashAlgorithmId = hashAlgorithmId,
                emailValidationStatus = EmailValidationStatus.NOT_CONFIRMED,
                confirmationToken = null,
                confirmationTokenExpires = null,
                passwordRecoveryToken = null,
                passwordRecoveryTokenExpires = null,
            )
        }
    }
}