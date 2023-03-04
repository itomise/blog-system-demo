package com.itomise.test.factory

import com.itomise.blogDb.lib.dbQuery
import com.itomise.blogDb.repository.UserRepository
import com.itomise.core.domain.security.services.HashingService
import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.vo.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object UserFactory : KoinComponent {
    private val userRepository by inject<UserRepository>()
    private val hashingService by inject<HashingService>()

    data class TestUser(
        val id: UUID,
        val name: String,
        val email: String,
        val password: String
    )

    suspend fun create(
        name: String = "テストユーザー_${UUID.randomUUID()}",
        email: String = "${UUID.randomUUID()}@test.test",
        password: String = "${UUID.randomUUID()}"
    ): TestUser {
        val saltedHash = hashingService.generateSaltedHash(password)
        val user = User.from(
            id = UUID.randomUUID(),
            email = Email(email),
            profile = UserProfile(Username(name)),
            loginInfo = UserInternalLoginInfo(
                passwordHash = saltedHash.hash,
                passwordSalt = saltedHash.salt,
                hashAlgorithmId = UserHashAlgorithmId.get(saltedHash.algorithm.value)
            )
        )

        dbQuery {
            userRepository.save(user)
        }

        return TestUser(user.id, name, email, password)
    }

    suspend fun activate(
        userId: UUID,
        name: String,
        password: String
    ): TestUser {
        val activatedUser = dbQuery {
            val user = userRepository.findByUserId(userId)!!

            val activatedUser = user.activateAsInternal(Username(name), password)

            userRepository.save(activatedUser)
            activatedUser
        }

        return TestUser(userId, name, activatedUser.email.value, password)
    }
}