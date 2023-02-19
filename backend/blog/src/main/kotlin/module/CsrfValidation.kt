package com.itomise.blog.module

import com.itomise.shared.module.RequestLoggingPlugin
import io.ktor.server.application.*

fun Application.csrfValidation() {
    if (!blogEnvConfig.isTest) {
        install(RequestLoggingPlugin)
    }
}