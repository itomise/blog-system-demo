package com.itomise.com.itomise.domain.account.interfaces

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserId

interface IUserRepository {
    suspend fun getList(): List<User>

    suspend fun findByUserId(id: UserId): User?

    suspend fun findByEmail(email: Email): User?

    suspend fun save(user: User)

    suspend fun delete(user: User)
}
