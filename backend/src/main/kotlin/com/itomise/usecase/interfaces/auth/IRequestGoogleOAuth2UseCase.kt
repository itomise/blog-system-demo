package com.itomise.com.itomise.usecase.interfaces.auth

interface IRequestGoogleOAuth2UseCase {
    fun handle(): OutputDto
    
    data class OutputDto(
        val state: String,
        val authenticationURI: String
    )
}