package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.UserId
import com.itomise.com.itomise.infrastructure.dao.UserDaoEntity
import com.itomise.com.itomise.usercase.interfaces.user.GetUserUseCaseOutputDto
import com.itomise.com.itomise.usercase.interfaces.user.GetUserUseCaseOutputDtoUser
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.infrastructure.dbQuery

class GetUserInteractor : IGetUserUseCase {
    override fun handle(): GetUserUseCaseOutputDto {
        val users = dbQuery {
            UserDaoEntity.all().map { UserEntity(UserId(it.id.value), it.name) }
        }

        return GetUserUseCaseOutputDto(
            users = users.map { GetUserUseCaseOutputDtoUser(it.id.value, it.name) }
        )
    }
}