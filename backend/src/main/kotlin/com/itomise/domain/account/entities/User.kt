package com.itomise.com.itomise.domain.account.entities

import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.util.getKoinInstance

data class User internal constructor(
    val id: UserId,
    val email: Email,
    val profile: UserProfile?,
    val loginInfo: UserLoginInfo?
) {
    private val hashService = getKoinInstance<IHashingService>()

    val isActive = profile != null && loginInfo != null

    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + profile.hashCode()
        return result
    }

    fun changeProfile(name: Username) = this.copy(profile = profile?.copy(name = name))

    fun activateAsInternal(name: Username, password: String): User {
        val saltedHash = hashService.generateSaltedHash(password)

        return this.copy(
            profile = UserProfile(
                name = name
            ),
            loginInfo = UserInternalLoginInfo(
                passwordHash = saltedHash.hash,
                passwordSalt = saltedHash.salt,
                hashAlgorithmId = UserHashAlgorithmId.get(saltedHash.algorithm.value),
            )
        )
    }

    fun setExternalLoginInfo(externalServiceType: UserExternalLoginInfo.ExternalServiceType): User {
        return this.copy(
            loginInfo = UserExternalLoginInfo(
                externalServiceType = externalServiceType
            )
        )
    }

    companion object {
        fun new(email: Email) = User(
            id = UserId.new(),
            email = email,
            profile = null,
            loginInfo = null
        )

        fun from(
            id: UserId,
            email: Email,
            profile: UserProfile?,
            loginInfo: UserLoginInfo?
        ): User {
            return User(
                id = id,
                email = email,
                profile = profile,
                loginInfo = loginInfo
            )
        }
    }
}
