package com.itomise.com.itomise.controller.responseModel

import java.util.*

data class GetListUserResponseModel(val users: List<GetListUserResponseModelUser>)

data class GetListUserResponseModelUser(val id: UUID, val name: String, val email: String)

data class GetUserResponseModel(val id: UUID, val name: String, val email: String)

data class CreateUserResponseModel(val id: UUID)