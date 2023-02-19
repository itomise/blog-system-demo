package com.itomise.blog.module

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val useCaseModule = module {

}

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(useCaseModule)
    }
}