package com.itomise.com.itomise.module

import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.services.UserService
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.domain.security.interfaces.INestedJwtTokenService
import com.itomise.com.itomise.domain.security.services.HashingService
import com.itomise.com.itomise.domain.security.services.JwtTokenService
import com.itomise.com.itomise.domain.security.services.NestedJwtTokenTokenService
import com.itomise.com.itomise.infrastructure.repositories.account.UserRepository
import com.itomise.com.itomise.usecase.interactors.account.*
import com.itomise.com.itomise.usecase.interactors.auth.ActivateUserInteractor
import com.itomise.com.itomise.usecase.interactors.auth.LoginInteractor
import com.itomise.com.itomise.usecase.interactors.auth.MeInteractor
import com.itomise.com.itomise.usecase.interactors.auth.SignUpInteractor
import com.itomise.com.itomise.usecase.interactors.mail.SendSignUpMailInteractor
import com.itomise.com.itomise.usecase.interfaces.account.*
import com.itomise.com.itomise.usecase.interfaces.auth.IActivateUserUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.ILoginUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.IMeUseCase
import com.itomise.com.itomise.usecase.interfaces.auth.ISignUpUseCase
import com.itomise.com.itomise.usecase.interfaces.mail.ISendSignUpMailUseCase
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    single<IGetAccountListUseCase> { GetAccountListInteractor() }
    single<ICreateAccountUseCase> { CreateAccountInteractor() }
    single<IUpdateAccountUseCase> { UpdateAccountInteractor() }
    single<IDeleteAccountUseCase> { DeleteAccountInteractor() }
    single<IGetAccountUseCase> { GetAccountInteractor() }
    single<ILoginUseCase> { LoginInteractor() }
    single<IMeUseCase> { MeInteractor() }
    single<ISendSignUpMailUseCase> { SendSignUpMailInteractor() }
    single<ISignUpUseCase> { SignUpInteractor() }
    single<IActivateUserUseCase> { ActivateUserInteractor() }
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