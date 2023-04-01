package com.itomise.blogDb.lib

import com.itomise.blogDb.repository.SessionRepository
import com.itomise.core.domain.security.entities.Session
import io.ktor.server.sessions.*
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.util.*

class SessionStorageDatabase : SessionStorage, KoinComponent {
    private val logger = LoggerFactory.getLogger(SessionStorageDatabase::class.java)
    private val sessionRepository by inject<SessionRepository>()

    // NOTE: ktor の Session を扱い処理が value を map みたいな形式で扱うが、永続化はプリミティブに行いたいので無理やり splitしている
    // ktor の仕様変更で壊れやすくなりそうなので要注意
    private val ktorSessionValuePrefix = "id=%23s"

    override suspend fun write(id: String, value: String) = runBlocking {
        val userIdString = value.substringAfterLast(ktorSessionValuePrefix, "")
        val userId = UUID.fromString(userIdString)

        val newSession = dbQuery {
            // このユーザーの session が既にあったら削除する
            sessionRepository.findByUserId(userId)?.let {
                sessionRepository.delete(it.id)
            }

            val newSession = Session.new(id = id, userId = userId)

            sessionRepository.save(newSession)
            newSession
        }

        logger.info("session was created. session: $newSession")
    }

    override suspend fun read(id: String): String = runBlocking {
        val session = dbQuery {
            sessionRepository.findById(id)
        }

        if (session != null) {
            if (!session.isValidExpireAt()) {
                invalidate(id)
                logger.info("session $id is expired.")
                throw NoSuchElementException()
            }

            return@runBlocking ktorSessionValuePrefix + session.userId.toString()
        } else {
            logger.info("Session $id is not found.")
            throw NoSuchElementException()
        }
    }

    override suspend fun invalidate(id: String) = runBlocking {
        dbQuery {
            sessionRepository.delete(id)
        }
        logger.info("session $id was deleted.")
    }
}