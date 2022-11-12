package com.itomise.com.itomise.domain.auth

import io.ktor.server.auth.*

data class UserPrincipal(val id: String) : Principal
