package com.itomise.com.itomise.controller.requestModels

import java.util.*

data class CreatePostRequestModel(
    val title: String,
    val content: String,
)

data class UpdatePostRequestModel(
    val title: String,
    val content: String,
)

data class DeletePostRequestModel(
    val id: UUID
)