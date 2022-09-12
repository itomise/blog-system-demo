package com.itomise.com.itomise.plugin

import com.itomise.com.itomise.usercase.interactors.user.CreateUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.DeleteUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.GetUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.UpdateUserInteractor
import com.itomise.com.itomise.usercase.interfaces.user.ICreateUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateUserUseCase
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    singleOf(::GetUserInteractor) { bind<IGetUserUseCase>() }
    singleOf(::CreateUserInteractor) { bind<ICreateUserUseCase>() }
    singleOf(::UpdateUserInteractor) { bind<IUpdateUserUseCase>() }
    singleOf(::DeleteUserInteractor) { bind<IDeleteUserUseCase>() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule)
    }
}