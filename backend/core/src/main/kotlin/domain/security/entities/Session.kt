package com.itomise.core.domain.security.entities

import java.time.LocalDateTime
import java.util.*

typealias SessionId = String

data class Session internal constructor(
    val id: SessionId,
    val userId: UUID,
    val expireAt: LocalDateTime
) {
    fun isValidExpireAt() = expireAt > LocalDateTime.now()

    fun refresh() = this.copy(
        expireAt = LocalDateTime.now()
    )

    companion object {
        const val SESSION_EXPIRATION_SECONDS: Long = 60 * 60 * 24 * 30

        fun new(id: SessionId, userId: UUID) = Session(
            id = id,
            userId = userId,
            expireAt = LocalDateTime.now().plusSeconds(SESSION_EXPIRATION_SECONDS)
        )

        fun from(
            id: String,
            userId: UUID,
            expireAt: LocalDateTime
        ) = Session(
            id = id,
            userId = userId,
            expireAt = expireAt
        )
    }
}
