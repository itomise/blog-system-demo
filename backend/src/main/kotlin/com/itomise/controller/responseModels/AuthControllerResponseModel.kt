package com.itomise.com.itomise.controller.responseModel

import java.util.*

data class SignUpResponseModel(
    val userId: UUID
)

data class MeResponseModel(val id: UUID, val name: String, val email: String)