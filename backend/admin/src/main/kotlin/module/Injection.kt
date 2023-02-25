package com.itomise.admin.module

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.services.UserService
import com.itomise.admin.domain.security.interfaces.IHashingService
import com.itomise.admin.domain.security.interfaces.IJwtTokenService
import com.itomise.admin.domain.security.interfaces.INestedJwtTokenService
import com.itomise.admin.domain.security.services.HashingService
import com.itomise.admin.domain.security.services.JwtTokenService
import com.itomise.admin.domain.security.services.NestedJwtTokenTokenService
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {

}

val repositoryModule = module {
    single<IUserRepository> { UserRepository() }
}

val serviceModule = module {
    single<IUserService> { UserService() }
    single<IJwtTokenService> { JwtTokenService() }
    single<IHashingService> { HashingService() }
    single<INestedJwtTokenService> { NestedJwtTokenTokenService() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule + repositoryModule + serviceModule)
    }
}