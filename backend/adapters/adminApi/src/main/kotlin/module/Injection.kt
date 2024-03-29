package com.itomise.adminApi.module

import com.itomise.blogDb.repository.PostRepository
import com.itomise.blogDb.repository.SessionRepository
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.security.services.HashingService
import com.itomise.core.domain.security.services.JwtTokenService
import com.itomise.core.domain.security.services.NestedJwtTokenTokenService
import com.itomise.core.domain.user.services.UserService
import com.itomise.core.lib.google.GoogleOAuth2Authentication
import com.itomise.eventBus.event.SendSignUpMailInteractor
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {
    single { SendSignUpMailInteractor() }
}

val repositoryModule = module {
    single { UserRepository() }
    single { PostRepository() }
    single { SessionRepository() }
}

val serviceModule = module {
    single { UserService() }
    single { JwtTokenService() }
    single { HashingService() }
    single { NestedJwtTokenTokenService() }
    single {
        GoogleOAuth2Authentication(
            oauth2ClientId = adminApiEnvConfig.google.oauth2ClientId,
            oauth2ClientSecret = adminApiEnvConfig.google.oauth2ClientSecret,
            callbackUrl = adminApiEnvConfig.urls.googleOAuth2CallbackUrl
        )
    }
}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule + repositoryModule + serviceModule)
    }
}