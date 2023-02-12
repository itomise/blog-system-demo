package com.itomise.admin.module

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.interfaces.IUserService
import com.itomise.admin.domain.account.services.UserService
import com.itomise.admin.domain.post.interfaces.IPostRepository
import com.itomise.admin.domain.security.interfaces.IHashingService
import com.itomise.admin.domain.security.interfaces.IJwtTokenService
import com.itomise.admin.domain.security.interfaces.INestedJwtTokenService
import com.itomise.admin.domain.security.services.HashingService
import com.itomise.admin.domain.security.services.JwtTokenService
import com.itomise.admin.domain.security.services.NestedJwtTokenTokenService
import com.itomise.admin.infrastructure.repositories.account.UserRepository
import com.itomise.admin.infrastructure.repositories.post.PostRepository
import com.itomise.admin.usecase.interactors.account.*
import com.itomise.admin.usecase.interactors.auth.*
import com.itomise.admin.usecase.interactors.mail.SendSignUpMailInteractor
import com.itomise.admin.usecase.interactors.post.*
import com.itomise.admin.usecase.interfaces.account.*
import com.itomise.admin.usecase.interfaces.auth.*
import com.itomise.admin.usecase.interfaces.mail.ISendSignUpMailUseCase
import com.itomise.admin.usecase.interfaces.post.*
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
    single<IRequestGoogleOAuth2UseCase> { RequestGoogleOAuth2Interactor() }
    single<ICallbackGoogleOAuth2UseCase> { CallbackGoogleOAuth2Interactor() }
    single<IGetListPostUseCase> { GetListPostInteractor() }
    single<IGetPostUseCase> { GetPostInteractor() }
    single<IUpdatePostUseCase> { UpdatePostInteractor() }
    single<IDeletePostUseCase> { DeletePostInteractor() }
    single<ICreatePostUseCase> { CreatePostInteractor() }
    single<IPublishPostUseCase> { PublishPostInteractor() }
    single<IUnPublishPostUseCase> { UnPublishPostInteractor() }
}

val repositoryModule = module {
    single<IUserRepository> { UserRepository() }
    single<IPostRepository> { PostRepository() }
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