package com.itomise.com.itomise.usecase.interfaces.post

import java.util.*

interface ICreatePostUseCase {
    suspend fun handle(command: Command): UUID

    data class Command(
        val title: String,
        val content: String
    )
}