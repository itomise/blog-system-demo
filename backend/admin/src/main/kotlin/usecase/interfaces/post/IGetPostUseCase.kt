package com.itomise.admin.usecase.interfaces.post

import java.util.*

interface IGetPostUseCase {
    suspend fun handle(id: UUID): OutputDto?

    data class OutputDto(
        val id: UUID,
        val title: String,
        val content: String,
        val status: Int
    )
}