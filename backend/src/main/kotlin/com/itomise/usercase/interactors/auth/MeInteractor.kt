package com.itomise.com.itomise.usercase.interactors.auth

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.usercase.interfaces.auth.IMeUseCase
import com.itomise.com.itomise.util.getKoinInstance
import com.itomise.infrastructure.dbQuery
import java.util.*

class MeInteractor : IMeUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(userId: String): IMeUseCase.OutputDtoUser? {
        val user = dbQuery {
            userRepository.findByUserId(UUID.fromString(userId))
        } ?: return null

        return IMeUseCase.OutputDtoUser(
            id = user.id,
            name = user.name,
            email = user.email.value,
        )
    }
}