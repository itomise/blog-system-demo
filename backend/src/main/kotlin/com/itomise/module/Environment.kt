package com.itomise.com.itomise.module

import io.ktor.server.application.*

lateinit var envConfig: EnvConfig

data class EnvConfig(
    val isTest: Boolean,
    val allowHost: String,
    val db: EnvConfigDb,
    val redis: EnvConfigRedis,
    val session: EnvConfigSession,
    val jwt: EnvConfigJwt,
    val sendGridApiKey: String,
    val urls: Urls
) {
    data class EnvConfigDb(
        val url: String,
        val user: String,
        val password: String
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
        val accountConfirmUrl: String
    )
}

fun Application.configureEnvironmentVariables() {
    fun fromConfig(path: String): String =
        environment.config.propertyOrNull(path)?.getString()
            ?: throw IllegalStateException("ktorの環境変数が見つかりません。 path: $path")

    envConfig = EnvConfig(
        isTest = environment.config.propertyOrNull("app.test") != null,
        allowHost = fromConfig("app.allowHost"),
        db = EnvConfig.EnvConfigDb(
            url = fromConfig("app.db.url"),
            user = fromConfig("app.db.user"),
            password = fromConfig("app.db.password"),
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
        sendGridApiKey = fromConfig("app.sendGridApiKey"),
        urls = EnvConfig.Urls(
            accountSignUpUrl = fromConfig("app.urls.accountSignInUrl"),
            accountConfirmUrl = fromConfig("app.urls.accountConfirmUrl")
        )
    )
}