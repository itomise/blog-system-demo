package com.itomise.admin.domain.user.vo

import io.ktor.server.auth.*

data class UserPrincipal(val id: String) : Principal
