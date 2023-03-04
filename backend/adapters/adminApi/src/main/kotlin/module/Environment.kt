package com.itomise.adminApi.module

import io.ktor.server.application.*
import io.ktor.server.config.*

lateinit var adminApiEnvConfig: AdminEnvConfig

data class AdminEnvConfig(
    val isTest: Boolean,
    val allowHost: String,
    val db: EnvConfigDb,
    val redis: EnvConfigRedis,
    val session: EnvConfigSession,
    val jwt: EnvConfigJwt,
    val urls: Urls,
    val sendGrid: SendGridConfig,
    val google: GoogleConfig
) {
    data class EnvConfigDb(
        val url: String,
        val user: String,
        val password: String,
        val instanceUnixSocket: String?,
        val instanceConnectionName: String?,
    )

    data class EnvConfigRedis(
        val endpoint: String
    )

    data class EnvConfigSession(
        val signKey: String
    )

    data class EnvConfigJwt(
        val privateKey: String,
        val publicKeyId: String,
        val issuer: String,
        val audience: String,
        val realm: String,
        val encryptionKey: String
    )

    data class Urls(
        val accountSignUpUrl: String,
        val accountActivateUrl: String,
        val adminRootUrl: String,
        val googleOAuth2CallbackUrl: String,
    )

    data class SendGridConfig(
        val apiKey: String
    )

    data class GoogleConfig(
        val oauth2ClientId: String,
        val oauth2ClientSecret: String,
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

    adminApiEnvConfig = AdminEnvConfig(
        isTest = config.propertyOrNull("app.test") != null,
        allowHost = fromConfig("app.allowHost"),
        db = AdminEnvConfig.EnvConfigDb(
            url = fromConfig("app.db.url"),
            user = fromConfig("app.db.user"),
            password = fromConfig("app.db.password"),
            instanceUnixSocket = fromConfigOrNull("app.db.instanceUnixSocket"),
            instanceConnectionName = fromConfigOrNull("app.db.instanceConnectionName"),
        ),
        redis = AdminEnvConfig.EnvConfigRedis(
            endpoint = fromConfig("app.redis.endpoint"),
        ),
        session = AdminEnvConfig.EnvConfigSession(
            signKey = fromConfig("app.sessionSignKey"),
        ),
        jwt = AdminEnvConfig.EnvConfigJwt(
            privateKey = fromConfig("app.jwt.privateKey"),
            publicKeyId = fromConfig("app.jwt.publicKeyId"),
            issuer = fromConfig("app.jwt.issuer"),
            audience = fromConfig("app.jwt.audience"),
            realm = fromConfig("app.jwt.realm"),
            encryptionKey = fromConfig("app.jwt.encryptionKey")
        ),
        urls = AdminEnvConfig.Urls(
            accountSignUpUrl = fromConfig("app.urls.accountSignInUrl"),
            accountActivateUrl = fromConfig("app.urls.accountActivateUrl"),
            adminRootUrl = fromConfig("app.urls.adminRootUrl"),
            googleOAuth2CallbackUrl = fromConfig("app.urls.googleOAuth2CallbackUrl")
        ),
        sendGrid = AdminEnvConfig.SendGridConfig(
            apiKey = fromConfig("sendGrid.apiKey"),
        ),
        google = AdminEnvConfig.GoogleConfig(
            oauth2ClientId = fromConfig("google.oauth2ClientId"),
            oauth2ClientSecret = fromConfig("google.oauth2ClientSecret"),
        )
    )
}