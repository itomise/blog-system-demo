package com.itomise.com.itomise.domain.account.vo

data class UserInternalLoginInfo(
    val passwordHash: String,
    val passwordSalt: String,
    val hashAlgorithmId: UserHashAlgorithmId,
) : UserLoginInfo