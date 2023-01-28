package com.itomise.com.itomise.controller.requestModels

data class LoginRequestModel(val email: String, val password: String)

data class SignUpRequestModel(val email: String)

data class ActivateRequestModel(val name: String, val token: String, val password: String?)

data class JwtLoginResponseModel(val token: String)