package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.lib.google.GoogleAuthentication
import com.itomise.admin.usecase.interfaces.auth.IRequestGoogleOAuth2UseCase
import java.math.BigInteger
import java.security.SecureRandom

class RequestGoogleOAuth2Interactor : IRequestGoogleOAuth2UseCase {
    override fun handle(): IRequestGoogleOAuth2UseCase.OutputDto {
        val state = BigInteger(130, SecureRandom()).toString(32)

        val authenticationURI = GoogleAuthentication.createOpenConnectAuthURI(state)
        
        return IRequestGoogleOAuth2UseCase.OutputDto(
            state = state,
            authenticationURI = authenticationURI
        )
    }
}