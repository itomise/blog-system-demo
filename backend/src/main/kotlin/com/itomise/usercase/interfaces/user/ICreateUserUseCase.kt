package com.itomise.com.itomise.usercase.interfaces.user

interface ICreateUserUseCase {
    fun handle(id: String, name: String)
}