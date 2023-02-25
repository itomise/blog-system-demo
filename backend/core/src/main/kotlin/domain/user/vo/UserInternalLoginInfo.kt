package com.itomise.core.domain.user.vo

data class UserInternalLoginInfo(
    val passwordHash: String,
    val passwordSalt: String,
    val hashAlgorithmId: UserHashAlgorithmId,
) : UserLoginInfo