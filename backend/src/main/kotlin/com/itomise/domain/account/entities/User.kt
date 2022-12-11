package com.itomise.com.itomise.domain.account.entities

import com.itomise.com.itomise.domain.account.vo.*
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.util.getKoinInstance
import java.time.LocalDateTime

data class User private constructor(
    val id: UserId,
    val email: Email,
    val name: Username,
    val loginInfo: UserLoginInfo
) {
    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return this.id == other.id
        }
        return false
    }

    fun changeName(name: Username) = this.copy(name = name)

    companion object {
        private val hashService = getKoinInstance<IHashingService>()

        fun new(name: Username, email: Email, password: String): User {
            val userId = UserId.new()

            val saltedHash = hashService.generateSaltedHash(password)

            val userLoginInfo = UserLoginInfo(
                userId = userId,
                email = email,
                passwordHash = saltedHash.hash,
                passwordSalt = saltedHash.salt,
                userHashAlgorithmId = UserHashAlgorithmId.get(saltedHash.algorithm.value),
                emailValidationStatus = EmailValidationStatus.NOT_CONFIRMED,
                confirmationToken = null,
                confirmationTokenExpires = null,
                passwordRecoveryToken = null,
                passwordRecoveryTokenExpires = null,
            )
            return User(
                id = userId,
                name = name,
                email = email,
                loginInfo = userLoginInfo
            )
        }

        fun from(
            id: UserId,
            email: Email,
            name: Username,
            passwordHash: String,
            passwordSalt: String,
            userHashAlgorithmId: UserHashAlgorithmId,
            emailValidationStatus: EmailValidationStatus,
            confirmationToken: String?,
            confirmationTokenExpires: LocalDateTime?,
            passwordRecoveryToken: String?,
            passwordRecoveryTokenExpires: LocalDateTime?,
        ) = User(
            id = id,
            email = email,
            name = name,
            loginInfo = UserLoginInfo(
                userId = id,
                email = email,
                passwordHash = passwordHash,
                passwordSalt = passwordSalt,
                userHashAlgorithmId = userHashAlgorithmId,
                emailValidationStatus = emailValidationStatus,
                confirmationToken = confirmationToken,
                confirmationTokenExpires = confirmationTokenExpires,
                passwordRecoveryToken = passwordRecoveryToken,
                passwordRecoveryTokenExpires = passwordRecoveryTokenExpires,
            )
        )
    }
}
