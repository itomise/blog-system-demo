package com.itomise.com.itomise.domain.account.vo

import io.ktor.server.auth.*

data class UserPrincipal(val id: String) : Principal