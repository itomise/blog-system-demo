package com.itomise

import com.itomise.com.itomise.module.injection
import com.itomise.com.itomise.utils.dao.DataBaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.itomise.plugins.*
import com.itomise.controller.*
import io.ktor.server.application.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        userRouting()
        module()
        injection()
    }.start(wait = true)
}

fun Application.module() {
    DataBaseFactory.init()
}