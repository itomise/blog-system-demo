package com.itomise.admin.module

import io.ktor.server.application.*

lateinit var envConfig: EnvConfig

data class EnvConfig(
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
    fun fromConfig(path: String): String =
        environment.config.propertyOrNull(path)?.getString()
            ?: throw IllegalStateException("ktorの環境変数が見つかりません。 path: $path")

    fun fromConfigOrNull(path: String): String? =
        environment.config.propertyOrNull(path)?.getString()

    envConfig = EnvConfig(
        isTest = environment.config.propertyOrNull("app.test") != null,
        allowHost = fromConfig("app.allowHost"),
        db = EnvConfig.EnvConfigDb(
            url = fromConfig("app.db.url"),
            user = fromConfig("app.db.user"),
            password = fromConfig("app.db.password"),
            instanceUnixSocket = fromConfigOrNull("app.db.instanceUnixSocket"),
            instanceConnectionName = fromConfigOrNull("app.db.instanceConnectionName"),
        ),
        redis = EnvConfig.EnvConfigRedis(
            endpoint = fromConfig("app.redis.endpoint"),
        ),
        session = EnvConfig.EnvConfigSession(
            signKey = fromConfig("app.sessionSignKey"),
        ),
        jwt = EnvConfig.EnvConfigJwt(
            privateKey = fromConfig("app.jwt.privateKey"),
            publicKeyId = fromConfig("app.jwt.publicKeyId"),
            issuer = fromConfig("app.jwt.issuer"),
            audience = fromConfig("app.jwt.audience"),
            realm = fromConfig("app.jwt.realm"),
            encryptionKey = fromConfig("app.jwt.encryptionKey")
        ),
        urls = EnvConfig.Urls(
            accountSignUpUrl = fromConfig("app.urls.accountSignInUrl"),
            accountActivateUrl = fromConfig("app.urls.accountActivateUrl"),
            adminRootUrl = fromConfig("app.urls.adminRootUrl"),
            googleOAuth2CallbackUrl = fromConfig("app.urls.googleOAuth2CallbackUrl")
        ),
        sendGrid = EnvConfig.SendGridConfig(
            apiKey = fromConfig("sendGrid.apiKey"),
        ),
        google = EnvConfig.GoogleConfig(
            oauth2ClientId = fromConfig("google.oauth2ClientId"),
            oauth2ClientSecret = fromConfig("google.oauth2ClientSecret"),
        )
    )
}