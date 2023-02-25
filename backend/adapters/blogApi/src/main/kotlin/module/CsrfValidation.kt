package com.itomise.blogApi.module

import com.itomise.core.module.RequestLoggingPlugin
import io.ktor.server.application.*

fun Application.csrfValidation() {
    if (!blogApiEnvConfig.isTest) {
        install(RequestLoggingPlugin)
    }
}