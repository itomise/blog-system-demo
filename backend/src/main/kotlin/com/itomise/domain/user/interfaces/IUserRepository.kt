package com.itomise.com.itomise.domain.user.interfaces

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.User
import com.itomise.com.itomise.domain.user.vo.UserId

interface IUserRepository {
    suspend fun getList(): List<User>

    suspend fun findByUserId(id: UserId): User?

    suspend fun findByEmail(email: Email): User?

    suspend fun save(user: User)

    suspend fun delete(user: User)
}
