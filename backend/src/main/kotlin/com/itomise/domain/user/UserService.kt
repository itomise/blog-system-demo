package com.itomise.com.itomise.domain.user

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.interfaces.IUserRepository
import com.itomise.com.itomise.util.getKoinInstance

class UserService {
    private val userRepository = getKoinInstance<IUserRepository>()

    suspend fun duplicateEmail(email: Email): Boolean {
        return userRepository.findByEmail(email) != null
    }
}