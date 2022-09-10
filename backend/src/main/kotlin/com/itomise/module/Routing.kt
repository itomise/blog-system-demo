package com.itomise.com.itomise.module

import com.itomise.controller.userRouting
import io.ktor.server.application.*

fun Application.routing() {
    userRouting()
}
