package com.itomise.com.itomise.domain.auth.interfaces

import com.itomise.com.itomise.domain.auth.UserLoginInfo
import com.itomise.com.itomise.domain.common.vo.Email
import java.util.*


interface IUserLoginInfoRepository {
    suspend fun findByUserId(userId: UUID): UserLoginInfo?

    suspend fun findByEmail(email: Email): UserLoginInfo?

    suspend fun save(userLoginInfo: UserLoginInfo)
}