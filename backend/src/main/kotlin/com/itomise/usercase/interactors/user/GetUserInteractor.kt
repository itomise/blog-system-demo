package com.itomise.com.itomise.usercase.interactors.user

import com.itomise.com.itomise.domain.user.UserEntity
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

class GetUserInteractor: IGetUserUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override fun handle(): List<UserEntity> {
        return userRepository.getList()
    }
}