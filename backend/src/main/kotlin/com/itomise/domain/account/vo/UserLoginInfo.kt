package com.itomise.com.itomise.domain.account.vo

data class UserLoginInfo(
    val userId: UserId,
    val passwordHash: String,
    val passwordSalt: String,
    val userHashAlgorithmId: UserHashAlgorithmId,
)