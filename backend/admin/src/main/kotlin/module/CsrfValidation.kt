package com.itomise.admin.module

import com.itomise.shared.module.RequestLoggingPlugin
import io.ktor.server.application.*

fun Application.csrfValidation() {
    if (!adminEnvConfig.isTest) {
        install(RequestLoggingPlugin)
    }
}
