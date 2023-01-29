package com.itomise.com.itomise.controller.requestModels

data class CreatePostRequestModel(
    val title: String,
    val content: String,
)

data class UpdatePostRequestModel(
    val title: String,
    val content: String,
)

data class PublishPostRequestModel(
    val title: String,
    val content: String,
)

data class UnPublishPostRequestModel(
    val title: String,
    val content: String,
)