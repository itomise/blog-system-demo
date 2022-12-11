package com.itomise.com.itomise.domain.account.vo

import java.time.LocalDateTime

data class UserLoginInfo(
    val userId: UserId,
    val email: Email,
    val passwordHash: String,
    val passwordSalt: String,
    val userHashAlgorithmId: UserHashAlgorithmId,
    val emailValidationStatus: EmailValidationStatus,
    val confirmationToken: String?,
    val confirmationTokenExpires: LocalDateTime?,
    val passwordRecoveryToken: String?,
    val passwordRecoveryTokenExpires: LocalDateTime?,
)