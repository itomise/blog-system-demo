package com.itomise.admin.controller.responseModels

import java.util.*

data class GetListPostResponseModel(
    val posts: List<GetPostResponseModel>
)

data class GetPostResponseModel(
    val id: UUID,
    val title: String,
    val content: String,
    val displayContent: String,
    val status: Int,
)