package helper

import com.itomise.admin.domain.account.interfaces.IUserRepository
import helper.repository.InMemoryUserRepository
import org.koin.dsl.module

val InMemoryRepositoryModule = module {
    single<IUserRepository> { InMemoryUserRepository() }
}