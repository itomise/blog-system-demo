package com.itomise.admin.usecase.interfaces.mail

import com.itomise.admin.domain.account.entities.User

interface ISendSignUpMailUseCase {
    fun handle(user: User)
}