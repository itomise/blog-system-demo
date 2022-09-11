package com.itomise

import com.itomise.com.itomise.infrastructure.dao.DataBaseFactory
import com.itomise.com.itomise.plugin.injection
import com.itomise.com.itomise.plugin.logging
import com.itomise.com.itomise.plugin.routing
import com.itomise.com.itomise.plugin.serialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    serialization()
    injection()
    routing()
    logging()

    DataBaseFactory.init(
        url = environment.config.propertyOrNull("app.db.url")!!.getString(),
        user = environment.config.propertyOrNull("app.db.user")?.getString() ?: "",
        password = environment.config.propertyOrNull("app.db.password")?.getString() ?: ""
    )
}