package com.itomise.com.itomise.controller.requestModel

data class CreateUserRequestModel(val id: String, val name: String)

data class UpdateUserRequestModel(val id: String, val name: String, val email: String)

data class DeleteUserRequestModel(val id: String)