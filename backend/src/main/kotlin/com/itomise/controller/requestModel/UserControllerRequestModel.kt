package com.itomise.com.itomise.controller.requestModel

import java.util.*

data class CreateUserRequestModel(val name: String, val email: String, val password: String)

data class UpdateUserRequestModel(val id: UUID, val name: String, val email: String)

data class DeleteUserRequestModel(val id: UUID)