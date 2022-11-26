package com.itomise.com.itomise.infrastructure

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient

lateinit var redisClient: KredsClient

object RedisFactory {
    fun init(endpoint: String) {
        newClient(Endpoint.from(endpoint)).use { client ->
            redisClient = client
        }
    }
}