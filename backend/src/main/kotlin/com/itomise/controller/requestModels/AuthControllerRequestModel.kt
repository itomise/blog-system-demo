package com.itomise.com.itomise.controller.requestModels

data class LoginRequestModel(val email: String, val password: String)

data class SignUpRequestModel(val name: String, val email: String)

data class ActivateRequestModel(val token: String, val password: String)

data class JwtLoginResponseModel(val token: String)