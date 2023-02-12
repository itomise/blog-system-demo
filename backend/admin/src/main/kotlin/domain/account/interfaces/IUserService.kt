package com.itomise.admin.domain.account.interfaces

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.vo.UserId

interface IUserService {
    suspend fun isDuplicateUser(allUsers: List<User>, user: User): Boolean

    fun isValidPassword(password: String, user: User): Boolean

    fun generateActivationToken(user: User): String

    fun getUserIdFromActivationToken(token: String): UserId
}