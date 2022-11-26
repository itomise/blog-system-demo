package com.itomise

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.itomise.com.itomise.infrastructure.RedisFactory
import com.itomise.com.itomise.module.authentication
import com.itomise.com.itomise.module.injection
import com.itomise.com.itomise.module.routing
import com.itomise.com.itomise.module.statusPage
import com.itomise.infrastructure.DataBaseFactory
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    // serialization
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())  // support java.time.* types
        }
    }

    // logging
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }

    // cors
    val allowHost = getEnvConfig("app.allowHost")
    install(CORS) {
        allowHost(host = allowHost, schemes = listOf("http", "https"))
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("X-Requested-With")
        allowCredentials = true
    }

    injection()

    authentication()

    routing()

    statusPage()

    RedisFactory.init(getEnvConfig("app.redis.endpoint"))

    DataBaseFactory.init(
        url = getEnvConfig("app.db.url"),
        user = getEnvConfig("app.db.user"),
        password = getEnvConfig("app.db.password")
    )
}

fun Application.getEnvConfig(path: String): String = environment.config.propertyOrNull(path)!!.getString()