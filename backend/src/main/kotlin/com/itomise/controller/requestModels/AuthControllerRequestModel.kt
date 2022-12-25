package com.itomise.com.itomise.controller.requestModel

data class LoginRequestModel(val email: String, val password: String)

data class SignUpRequestModel(val name: String, val email: String, val password: String)

data class JwtLoginResponseModel(val token: String)