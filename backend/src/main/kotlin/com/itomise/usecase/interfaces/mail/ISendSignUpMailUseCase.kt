package com.itomise.com.itomise.usecase.interfaces.mail

import com.itomise.com.itomise.domain.account.entities.User

interface ISendSignUpMailUseCase {
    fun handle(user: User)
}