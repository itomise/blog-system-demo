package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.infrastructure.dao.UserDaoEntity
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import com.itomise.infrastructure.dbQuery

class UpdateUserInteractor : IUpdateUserUseCase {
    override fun handle(id: String, name: String) {
        dbQuery {
            val targetUser = UserDaoEntity.findById(id)
            if (targetUser != null) {
                targetUser.name = name
            } else {
                throw IllegalArgumentException("指定された userId は存在しません")
            }
        }
    }
}