package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.infrastructure.repositories.user.UserRepository
import com.itomise.com.itomise.usercase.interactors.auth.LoginInteractor
import com.itomise.com.itomise.usercase.interactors.auth.MeInteractor
import com.itomise.com.itomise.usercase.interactors.user.CreateUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.DeleteUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.GetUserInteractor
import com.itomise.com.itomise.usercase.interactors.user.UpdateUserInteractor
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
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
    singleOf(::LoginInteractor) { bind<ILoginUseCase>() }
    singleOf(::MeInteractor) { bind<IMeUseCase>() }
}

val repositoryModule = module {
    singleOf(::UserRepository) { bind<IUserRepository>() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule)
        modules(repositoryModule)
    }
}