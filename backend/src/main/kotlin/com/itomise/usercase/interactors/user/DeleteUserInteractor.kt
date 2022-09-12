package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.infrastructure.dao.UserDaoEntity
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteUserUseCase
import com.itomise.infrastructure.dbQuery

class DeleteUserInteractor : IDeleteUserUseCase {
    override fun handle(id: String) {
        dbQuery {
            val targetUser = UserDaoEntity.findById(id)
            if (targetUser != null) {
                targetUser.delete()
            } else {
                throw IllegalArgumentException("指定された userId は存在しません")
            }
        }
    }
}