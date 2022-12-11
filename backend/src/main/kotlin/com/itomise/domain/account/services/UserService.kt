package com.itomise.com.itomise.domain.account.services

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserRepository
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.Email
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.vo.HashAlgorithm
import com.itomise.com.itomise.domain.security.vo.SaltedHash
import com.itomise.com.itomise.util.getKoinInstance

class UserService : IUserService {
    private val userRepository = getKoinInstance<IUserRepository>()
    private val hashingService = getKoinInstance<IHashingService>()

    override suspend fun isDuplicateUserId(userId: UserId): Boolean {
        return userRepository.findByUserId(userId) != null
    }

    override suspend fun isDuplicateUserEmail(email: Email): Boolean {
        return userRepository.findByEmail(email) != null
    }

    override fun isValidPassword(password: String, user: User): Boolean {
        return hashingService.verify(
            value = password,
            saltedHash = SaltedHash(
                hash = user.loginInfo.passwordHash,
                salt = user.loginInfo.passwordSalt,
                algorithm = HashAlgorithm.get(user.loginInfo.userHashAlgorithmId.value)
            )
        )
    }
}