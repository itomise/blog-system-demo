package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.infrastructure.repositories.user.UserRepository
import com.itomise.com.itomise.usercase.interactors.user.GetUserInteractor
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    singleOf(::GetUserInteractor) { bind<IGetUserUseCase>() }
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