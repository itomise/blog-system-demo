package com.itomise.com.itomise.domain.account.vo

data class UserInternalLoginInfo(
    override val userId: UserId,
    val passwordHash: String,
    val passwordSalt: String,
    val userHashAlgorithmId: UserHashAlgorithmId,
) : UserLoginInfo