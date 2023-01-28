package com.itomise.com.itomise.usecase.interfaces.auth

import java.util.*

interface ICallbackGoogleOAuth2UseCase {
    suspend fun handle(code: String): OutputDto

    data class OutputDto(
        val userId: UUID,
        val isActiveUser: Boolean,
        val activateToken: String?
    )
}