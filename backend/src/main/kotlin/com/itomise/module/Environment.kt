package com.itomise.com.itomise.module

import io.ktor.server.application.*

lateinit var envConfig: EnvConfig

data class EnvConfig(
    val allowHost: String,
    val db: EnvConfigDb,
    val redis: EnvConfigRedis,
    val session: EnvConfigSession,
    val jwt: EnvConfigJwt
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
        val issuer: String,
        val audience: String,
        val realm: String,
    )
}

fun Application.configureEnvironmentVariables() {
    fun getConfig(path: String): String = environment.config.propertyOrNull(path)!!.getString()

    envConfig = EnvConfig(
        allowHost = getConfig("app.allowHost"),
        db = EnvConfig.EnvConfigDb(
            url = getConfig("app.db.url"),
            user = getConfig("app.db.user"),
            password = getConfig("app.db.password"),
        ),
        redis = EnvConfig.EnvConfigRedis(
            endpoint = getConfig("app.redis.endpoint"),
        ),
        session = EnvConfig.EnvConfigSession(
            signKey = getConfig("app.session.signKey"),
        ),
        jwt = EnvConfig.EnvConfigJwt(
            privateKey = getConfig("app.jwt.privateKey"),
            issuer = getConfig("app.jwt.issuer"),
            audience = getConfig("app.jwt.audience"),
            realm = getConfig("app.jwt.realm"),
        )
    )
}