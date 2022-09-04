package com.itomise.com.itomise.module

import com.itomise.com.itomise.usercase.interactors.user.GetUserInteractor
import com.itomise.com.itomise.usercase.interfaces.user.IGetUserUseCase
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val appModule = module {
    singleOf(::GetUserInteractor) { bind<IGetUserUseCase>() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(appModule)
    }
}