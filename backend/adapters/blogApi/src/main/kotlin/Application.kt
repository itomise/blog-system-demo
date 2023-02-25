package com.itomise.blogApi

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.itomise.blogApi.module.*
import com.itomise.blogDb.lib.DataBaseFactory
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureEnvironmentVariables()

    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())  // support java.time.* types
        }
    }

    // forwarded header ( https なしで secure な cookie を設定するために、リバースプロキシからくるヘッダを認識するために必要 )
    install(ForwardedHeaders)
    install(XForwardedHeaders) {
        useFirstProxy()
    }

    // logging
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            "Status: $status, HTTP method: $httpMethod, PATH: $path"
        }
    }

    val allowHost = blogApiEnvConfig.allowHost
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

    csrfValidation()

    routing()

    statusPage()

    DataBaseFactory.init(
        url = blogApiEnvConfig.db.url,
        user = blogApiEnvConfig.db.user,
        password = blogApiEnvConfig.db.password,
        instanceUnixSocket = blogApiEnvConfig.db.instanceUnixSocket,
        instanceConnectionName = blogApiEnvConfig.db.instanceConnectionName
    )
}