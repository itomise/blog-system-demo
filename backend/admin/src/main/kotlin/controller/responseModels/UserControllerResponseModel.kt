package com.itomise.admin.controller.responseModels

import java.util.*

data class GetListUserResponseModel(val users: List<GetListUserResponseModelUser>)

data class GetListUserResponseModelUser(val id: UUID, val name: String?, val email: String, val isActive: Boolean)

data class GetUserResponseModel(val id: UUID, val name: String?, val email: String, val isActive: Boolean)

data class CreateUserResponseModel(val id: UUID)