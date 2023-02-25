package com.itomise.blogDb.lib

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.server.sessions.*

lateinit var redisClient: KredsClient

object RedisFactory {
    const val SESSION_EXPIRES_DURATION: Long = 60 * 60 * 24 * 30
    fun init(endpoint: String) {
        newClient(Endpoint.from(endpoint)).use { client ->
            redisClient = client
        }
    }
}

class SessionStorageRedis : SessionStorage {
    private val EXPIRES_DURATION = RedisFactory.SESSION_EXPIRES_DURATION
    override suspend fun write(id: String, value: String) {
        redisClient.set(id, value)
        redisClient.expire(id, EXPIRES_DURATION.toULong())
    }

    override suspend fun read(id: String): String {
        val session = redisClient.get(id)
        if (session != null) {
            redisClient.expire(id, EXPIRES_DURATION.toULong())
            return session
        } else throw NoSuchElementException("Session $id not found")
    }

    override suspend fun invalidate(id: String) {
        redisClient.del(id)
    }
}