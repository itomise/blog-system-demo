package com.itomise

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.itomise.com.itomise.infrastructure.RedisFactory
import com.itomise.com.itomise.module.*
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
    configureEnvironmentVariables()

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
    val allowHost = envConfig.allowHost
    install(CORS) {
        allowHost(host = allowHost, schemes = listOf("http", "https"))
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("X-Requested-With")
        allowHeader("Authorization")
        allowCredentials = true
    }

    injection()

    authentication()

    routing()

    statusPage()

    RedisFactory.init(envConfig.redis.endpoint)

    DataBaseFactory.init(
        url = envConfig.db.url,
        user = envConfig.db.user,
        password = envConfig.db.password
    )
}
