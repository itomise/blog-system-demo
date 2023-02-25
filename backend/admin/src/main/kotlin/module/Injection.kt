package com.itomise.admin.module

import com.itomise.admin.domain.security.services.HashingService
import com.itomise.admin.domain.security.services.JwtTokenService
import com.itomise.admin.domain.security.services.NestedJwtTokenTokenService
import com.itomise.admin.domain.user.services.UserService
import com.itomise.admin.infrastructure.repositories.user.UserRepository
import com.itomise.admin.usecase.SendSignUpMailInteractor
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    single { SendSignUpMailInteractor() }
}

val repositoryModule = module {
    single { UserRepository() }
}

val serviceModule = module {
    single { UserService() }
    single { JwtTokenService() }
    single { HashingService() }
    single { NestedJwtTokenTokenService() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule + repositoryModule + serviceModule)
    }
}