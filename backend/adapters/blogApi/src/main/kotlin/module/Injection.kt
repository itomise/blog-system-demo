package com.itomise.blogApi.module

import com.itomise.blogDb.queryService.blog.GetPublishedPostsQueryService
import com.itomise.blogDb.repository.PostRepository
import com.itomise.blogDb.repository.UserRepository
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.injection() {
    install(Koin) {
        SLF4JLogger()
        modules(module {
            single { GetPublishedPostsQueryService() }
            single { PostRepository() }
            single { UserRepository() }
        })
    }
}