package com.itomise.blogDb.lib

import com.itomise.blogDb.repository.SessionRepository
import com.itomise.core.domain.security.entities.Session
import io.ktor.server.sessions.*
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class SessionStorageDatabase : SessionStorage, KoinComponent {
    private val sessionRepository by inject<SessionRepository>()

    // NOTE: ktor の Session を扱い処理が value を map みたいな形式で扱うが、永続化はプリミティブに行いたいので無理やり splitしている
    // ktor の仕様変更で壊れやすくなりそうなので要注意
    private val ktorSessionValuePrefix = "id=%23s"

    override suspend fun write(id: String, value: String) = runBlocking {
        val userId = value.substringAfterLast(ktorSessionValuePrefix, "")

        val session = Session.new(
            id = id,
            userId = UUID.fromString(userId),
        )

        dbQuery {
            sessionRepository.save(session)
        }
    }

    override suspend fun read(id: String): String = runBlocking {
        val session = dbQuery {
            sessionRepository.findById(id)
        }
        if (session != null) {
            if (!session.isValidExpireAt()) {
                runBlocking {
                    invalidate(id)
                }
                throw NoSuchElementException("Session $id is expired.")
            }

            val updatedSession = session.refresh()
            dbQuery {
                sessionRepository.save(updatedSession)
            }
            return@runBlocking ktorSessionValuePrefix + updatedSession.userId.toString()
        } else throw NoSuchElementException("Session $id not found.")
    }

    override suspend fun invalidate(id: String) = runBlocking {
        dbQuery {
            sessionRepository.delete(id)
        }
    }
}