package com.itomise.com.itomise.infrastructure.repositories.user

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.UserId
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.infrastructure.dao.UserDao
import com.itomise.infrastructure.dbQuery
import org.jetbrains.exposed.sql.selectAll

class UserRepository : IUserRepository {
    override fun getList(): List<UserEntity> = dbQuery {
        UserDao.selectAll().map { UserEntity(UserId(it[UserDao.id]), it[UserDao.name]) }
    }

    override suspend fun update(): UserId {
        TODO("Not yet implemented")
    }
}