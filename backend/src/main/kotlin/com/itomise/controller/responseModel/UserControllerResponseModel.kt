package com.itomise.com.itomise.controller.responseModel

data class GetUserListResponseModel(val users: List<GetUserResponseModel>)

data class GetUserResponseModel(val id: String, val name: String)