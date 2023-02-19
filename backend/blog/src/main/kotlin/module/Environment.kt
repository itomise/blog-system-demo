package com.itomise.blog.module

import io.ktor.server.application.*
import io.ktor.server.config.*

lateinit var blogEnvConfig: BlogEnvironment

data class BlogEnvironment(
    val isTest: Boolean,
    val allowHost: String,
    val db: EnvConfigDb
) {
    data class EnvConfigDb(
        val url: String,
        val user: String,
        val password: String,
        val instanceUnixSocket: String?,
        val instanceConnectionName: String?,
    )
}

fun Application.configureEnvironmentVariables() {
    initializeEnvConfig(environment.config)
}

fun initializeEnvConfig(config: ApplicationConfig) {
    fun fromConfig(path: String): String =
        config.propertyOrNull(path)?.getString()
            ?: throw IllegalStateException("ktorの環境変数が見つかりません。 path: $path")

    fun fromConfigOrNull(path: String): String? =
        config.propertyOrNull(path)?.getString()

    blogEnvConfig = BlogEnvironment(
        isTest = config.propertyOrNull("app.test") != null,
        allowHost = fromConfig("app.allowHost"),
        db = BlogEnvironment.EnvConfigDb(
            url = fromConfig("app.db.url"),
            user = fromConfig("app.db.user"),
            password = fromConfig("app.db.password"),
            instanceUnixSocket = fromConfigOrNull("app.db.instanceUnixSocket"),
            instanceConnectionName = fromConfigOrNull("app.db.instanceConnectionName"),
        )
    )
}