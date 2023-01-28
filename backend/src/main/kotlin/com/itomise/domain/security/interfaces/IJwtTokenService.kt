package com.itomise.com.itomise.domain.security.interfaces

import com.auth0.jwt.interfaces.DecodedJWT
import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.domain.security.vo.TokenConfig
import java.security.PrivateKey

interface IJwtTokenService {
    fun generate(
        config: TokenConfig,
        privateKey: PrivateKey,
        vararg claims: TokenClaim
    ): String

    fun verify(
        config: TokenConfig,
        token: String
    ): DecodedJWT?
}