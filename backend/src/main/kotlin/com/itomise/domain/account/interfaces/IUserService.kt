package com.itomise.com.itomise.domain.account.interfaces

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserId

interface IUserService {
    suspend fun isDuplicateUserId(userId: UserId): Boolean

    suspend fun isDuplicateUserEmail(email: Email): Boolean

    fun isValidPassword(password: String, user: User): Boolean
}