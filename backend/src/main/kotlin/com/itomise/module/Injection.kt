package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.services.UserService
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.interfaces.ITokenService
import com.itomise.com.itomise.domain.security.services.HashingService
import com.itomise.com.itomise.domain.security.services.TokenService
import com.itomise.com.itomise.infrastructure.repositories.account.UserRepository
import com.itomise.com.itomise.usercase.interactors.account.CreateAccountInteractor
import com.itomise.com.itomise.usercase.interactors.account.DeleteAccountInteractor
import com.itomise.com.itomise.usercase.interactors.account.GetAccountListInteractor
import com.itomise.com.itomise.usercase.interactors.account.UpdateAccountInteractor
import com.itomise.com.itomise.usercase.interactors.auth.LoginInteractor
import com.itomise.com.itomise.usercase.interactors.auth.MeInteractor
import com.itomise.com.itomise.usercase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
import com.itomise.com.itomise.usercase.interfaces.user.ICreateAccountUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IDeleteAccountUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IGetAccountListUseCase
import com.itomise.com.itomise.usercase.interfaces.user.IUpdateAccountUseCase
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    singleOf(::GetAccountListInteractor) { bind<IGetAccountListUseCase>() }
    singleOf(::CreateAccountInteractor) { bind<ICreateAccountUseCase>() }
    singleOf(::UpdateAccountInteractor) { bind<IUpdateAccountUseCase>() }
    singleOf(::DeleteAccountInteractor) { bind<IDeleteAccountUseCase>() }
    singleOf(::LoginInteractor) { bind<ILoginUseCase>() }
    singleOf(::MeInteractor) { bind<IMeUseCase>() }
}

val repositoryModule = module {
    singleOf(::UserRepository) { bind<IUserRepository>() }
}

val serviceModule = module {
    singleOf(::UserService) { bind<IUserService>() }
    singleOf(::TokenService) { bind<ITokenService>() }
    singleOf(::HashingService) { bind<IHashingService>() }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule + repositoryModule + serviceModule)
    }
}