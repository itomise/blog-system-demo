package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.infrastructure.dao.UserDaoEntity
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.infrastructure.dbQuery

class CreateUserInteractor : ICreateUserUseCase {

    override fun handle(userId: String, name: String) {
        dbQuery {
            UserDaoEntity.new(userId) {
                this.name = name
            }
        }
    }
}