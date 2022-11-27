package com.itomise.com.itomise.domain.user.interfaces

import com.itomise.com.itomise.domain.user.User
import java.util.*

interface IUserRepository {
    suspend fun getList(): List<User>

    suspend fun findByUserId(id: UUID): User?

    suspend fun save(user: User)

    suspend fun delete(user: User)
}
