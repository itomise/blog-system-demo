package com.itomise.com.itomise.module

import com.itomise.com.itomise.controller.authRouting
import com.itomise.controller.userRouting
import io.ktor.server.application.*

fun Application.routing() {
    userRouting()
    authRouting()
}
