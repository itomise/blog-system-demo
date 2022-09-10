package com.itomise

import com.itomise.com.itomise.module.injection
import com.itomise.com.itomise.module.routing
import com.itomise.com.itomise.module.serialization
import com.itomise.com.itomise.utils.dao.DataBaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    serialization()
    injection()
    routing()

    DataBaseFactory.init(
        url = environment.config.propertyOrNull("db.url")!!.getString(),
        user = environment.config.propertyOrNull("db.user")?.getString() ?: "",
        password = environment.config.propertyOrNull("db.password")?.getString() ?: ""
    )
}