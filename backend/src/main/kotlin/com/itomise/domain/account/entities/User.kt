package com.itomise.com.itomise.domain.account.entities

import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.util.getKoinInstance

data class User internal constructor(
    val id: UserId,
    val email: Email,
    val name: Username,
    val loginInfo: UserInternalLoginInfo?
) {
    private val hashService = getKoinInstance<IHashingService>()

    val isActive = loginInfo != null

    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    fun changeName(name: Username) = this.copy(name = name)

    fun activate(password: String): User {
        val saltedHash = hashService.generateSaltedHash(password)

        return this.copy(
            loginInfo = UserInternalLoginInfo(
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
                UserInternalLoginInfo(
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
