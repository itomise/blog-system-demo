package com.itomise.com.itomise.domain.account.interfaces

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.vo.UserId

interface IUserService {
    suspend fun isDuplicateUser(allUsers: List<User>, user: User): Boolean

    fun isValidPassword(password: String, user: User): Boolean

    fun generateActivationToken(user: User): String

    fun getUserIdFromActivationTokenOrNull(token: String): UserId
}