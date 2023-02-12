package com.itomise.admin.controller.responseModels

import java.util.*

data class SignUpResponseModel(
    val userId: UUID
)

data class MeResponseModel(val id: UUID, val name: String?, val email: String, val isActive: Boolean)