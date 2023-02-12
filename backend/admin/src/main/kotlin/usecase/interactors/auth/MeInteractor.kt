package com.itomise.admin.usecase.interactors.auth

import com.itomise.admin.domain.account.interfaces.IUserRepository
import com.itomise.admin.domain.account.vo.UserId
import com.itomise.admin.usecase.interfaces.auth.IMeUseCase
import com.itomise.admin.util.getKoinInstance
import com.itomise.admin.infrastructure.dbQuery
import java.util.*

class MeInteractor : IMeUseCase {
    private val userRepository = getKoinInstance<IUserRepository>()

    override suspend fun handle(userId: String): IMeUseCase.OutputDtoUser? {
        val user = dbQuery {
            userRepository.findByUserId(UserId(UUID.fromString(userId)))
        } ?: return null

        return IMeUseCase.OutputDtoUser(
            id = user.id.value,
            name = user.profile?.name?.value,
            email = user.email.value,
            isActive = user.isActive
        )
    }
}