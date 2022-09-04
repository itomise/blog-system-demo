package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.UserId
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase

class GetUserInteractor: IGetUserUseCase {
    override fun handle(): UserEntity {
        return UserEntity(UserId("test"), "ほげ太郎")
    }
}