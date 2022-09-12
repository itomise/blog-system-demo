package com.itomise.com.itomise.usercase.interfaces.user

interface IUpdateUserUseCase {
    fun handle(id: String, name: String)
}
