package com.itomise.com.itomise.domain.account.entities

import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.util.getKoinInstance

data class User private constructor(
    val id: UserId,
    val email: Email,
    val name: Username,
    val loginInfo: UserLoginInfo?
) {
    private val hashService = getKoinInstance<IHashingService>()

    val isActive = loginInfo != null

    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return this.id == other.id
        }
        return false
    }

    fun changeName(name: Username) = this.copy(name = name)

    fun activate(password: String): User {
        val saltedHash = hashService.generateSaltedHash(password)

        return this.copy(
            loginInfo = UserLoginInfo(
                userId = this.id,
                passwordHash = saltedHash.hash,
                passwordSalt = saltedHash.salt,
                userHashAlgorithmId = UserHashAlgorithmId.get(saltedHash.algorithm.value),
            )
        )
    }

    companion object {
        fun new(name: Username, email: Email) = User(
            id = UserId.new(),
            name = name,
            email = email,
            loginInfo = null
        )

        fun from(
            id: UserId,
            email: Email,
            name: Username,
            passwordHash: String?,
            passwordSalt: String?,
            userHashAlgorithmId: UserHashAlgorithmId?,
        ): User {
            val loginInfo = if (passwordHash != null && passwordSalt != null && userHashAlgorithmId != null) {
                UserLoginInfo(
                    userId = id,
                    passwordHash = passwordHash,
                    passwordSalt = passwordSalt,
                    userHashAlgorithmId = userHashAlgorithmId,
                )
            } else null

            return User(
                id = id,
                email = email,
                name = name,
                loginInfo = loginInfo
            )
        }
    }
}
