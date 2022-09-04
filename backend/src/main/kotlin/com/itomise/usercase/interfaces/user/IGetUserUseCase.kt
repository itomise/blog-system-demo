package com.itomise.com.itomise.usercase.interfaces.user

import com.itomise.com.itomise.domain.user.UserEntity

interface IGetUserUseCase {
    fun handle(): UserEntity
}