package com.itomise.com.itomise.controller.requestModel

import java.util.UUID

data class CreateUserRequestModel(val id: UUID, val name: String, val email: String)

data class UpdateUserRequestModel(val id: UUID, val name: String, val email: String)

data class DeleteUserRequestModel(val id: UUID)