package com.itomise.com.itomise.infrastructure.repositories.user

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.UserId
import com.itomise.com.itomise.domain.user.UserTable
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.utils.dao.dbQuery
import org.jetbrains.exposed.sql.selectAll

class UserRepository : IUserRepository {
    override fun getList(): List<UserEntity> = dbQuery {
        UserTable.selectAll().map { UserEntity(UserId(it[UserTable.id]), it[UserTable.name])}
    }

    override suspend fun update(): UserId {
        TODO("Not yet implemented")
    }
}