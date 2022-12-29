package com.itomise.com.itomise.domain.security.interfaces

import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.domain.security.vo.TokenConfig

interface IJwtTokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}