package com.itomise.blogDb.repository

import com.itomise.blogDb.dao.SessionTable
import com.itomise.core.domain.security.entities.Session
import com.itomise.core.domain.security.entities.SessionId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class SessionRepository {
    private fun resultRowToPostEntity(row: ResultRow): Session {
        return Session.from(
            id = row[SessionTable.id].value,
            userId = row[SessionTable.userId],
            expireAt = row[SessionTable.expireAt]
        )
    }

    suspend fun findById(id: SessionId): Session? {
        return SessionTable
            .select(SessionTable.id eq id)
            .map(::resultRowToPostEntity)
            .firstOrNull()
    }

    suspend fun save(session: Session) {
        val isExists = findById(session.id) != null
        if (isExists) {
            SessionTable.update({
                SessionTable.id eq session.id
            }) { s ->
                s[userId] = session.userId
                s[expireAt] = session.expireAt
                s[updatedAt] = LocalDateTime.now()
            }
        } else {
            SessionTable.insert { s ->
                s[id] = session.id
                s[userId] = session.userId
                s[expireAt] = session.expireAt
            }
        }
    }

    suspend fun delete(id: SessionId) {
        SessionTable.deleteWhere {
            SessionTable.id eq id
        }
    }
}