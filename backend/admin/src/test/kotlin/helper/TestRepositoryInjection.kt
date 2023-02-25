package helper

import helper.repository.InMemoryUserRepository
import org.koin.dsl.module

val InMemoryRepositoryModule = module {
    single<IUserRepository> { InMemoryUserRepository() }
}