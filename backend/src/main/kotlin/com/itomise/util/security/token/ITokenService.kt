package com.itomise.com.itomise.util.security.token

interface ITokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}