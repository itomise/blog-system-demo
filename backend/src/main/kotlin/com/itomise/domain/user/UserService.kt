package com.itomise.com.itomise.domain.user

import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.util.getKoinInstance

class UserService {
    private val userRepository = getKoinInstance<IUserRepository>()

    suspend fun isDuplicateUser(user: User): Boolean {
        return userRepository.findByUserId(user.id) != null
    }
}