package com.itomise.com.itomise.domain.user.interfaces

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.UserId

interface IUserRepository {
    fun getList(): List<UserEntity>
    suspend fun update(): UserId
}