package com.itomise.com.itomise.usecase.interfaces.post

import java.util.*

interface IUpdatePostUseCase {
    suspend fun handle(command: Command)

    data class Command(
        val id: UUID,
        val title: String,
        val content: String,
    )
}