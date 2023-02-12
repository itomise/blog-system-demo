package com.itomise.admin.domain.account.entities

import com.itomise.admin.domain.account.vo.*
import com.itomise.admin.domain.security.interfaces.IHashingService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class User internal constructor(
    val id: UserId,
    val email: Email,
    val loginType: UserLoginType,
    val profile: UserProfile?,
    val loginInfo: UserLoginInfo?
) : KoinComponent {
    private val hashService by inject<IHashingService>()

    val isActive = profile != null && loginInfo != null

    init {
        if (loginInfo is UserExternalLoginInfo && loginInfo.externalServiceType == UserExternalLoginInfo.ExternalServiceType.GOOGLE) {
            require(loginType == UserLoginType.EXTERNAL_GOOGLE)
        } else {
            require(loginType == UserLoginType.INTERNAL)
        }
    }

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

    fun activateAsExternal(name: Username): User {
        require(loginType != UserLoginType.INTERNAL)

        return this.copy(
            profile = UserProfile(
                name = name
            ),
        )
    }

    fun activateAsInternal(name: Username, password: String): User {
        require(loginType == UserLoginType.INTERNAL)

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

    companion object {
        fun new(email: Email, loginType: UserLoginType = UserLoginType.INTERNAL): User {
            val loginInfo = when (loginType) {
                UserLoginType.EXTERNAL_GOOGLE -> UserExternalLoginInfo(
                    externalServiceType = UserExternalLoginInfo.ExternalServiceType.GOOGLE
                )

                else -> null
            }

            return User(
                id = UserId.new(),
                email = email,
                loginType = loginType,
                profile = null,
                loginInfo = loginInfo
            )
        }

        fun from(
            id: UserId,
            email: Email,
            profile: UserProfile?,
            loginInfo: UserLoginInfo?
        ): User {
            val loginType =
                if (loginInfo is UserExternalLoginInfo && loginInfo.externalServiceType == UserExternalLoginInfo.ExternalServiceType.GOOGLE) {
                    UserLoginType.EXTERNAL_GOOGLE
                } else UserLoginType.INTERNAL

            return User(
                id = id,
                email = email,
                loginType = loginType,
                profile = profile,
                loginInfo = loginInfo
            )
        }
    }
}
