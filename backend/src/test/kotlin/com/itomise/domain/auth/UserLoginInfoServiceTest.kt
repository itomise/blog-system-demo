package com.itomise.domain.auth

import com.itomise.com.itomise.domain.auth.EmailValidationStatus
import com.itomise.com.itomise.domain.auth.HashAlgorithm
import com.itomise.com.itomise.domain.auth.UserLoginInfo
import com.itomise.com.itomise.domain.auth.UserLoginInfoService
import com.itomise.com.itomise.domain.user.vo.Email
import com.itomise.com.itomise.module.repositoryModule
import com.itomise.com.itomise.module.useCaseModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserLoginInfoServiceTest {
    @BeforeTest
    fun prepare() {
        startKoin {
            modules(useCaseModule)
            modules(repositoryModule)
        }
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    @Test
    fun generatePasswordHashTest() {
        val password = UUID.randomUUID().toString()
        val salt = UUID.randomUUID().toString()
        val hashAlgorithmId = HashAlgorithm.getRandom()

        val hash1 = UserLoginInfoService.generatePasswordHash(
            password = password,
            salt = salt,
            hashAlgorithm = hashAlgorithmId
        )
        val hash2 = UserLoginInfoService.generatePasswordHash(
            password = password,
            salt = salt,
            hashAlgorithm = hashAlgorithmId
        )
        assert(hash1 == hash2)
    }

    @Test
    fun checkValidPasswordTest() {
        val password = UUID.randomUUID().toString()
        val salt = UUID.randomUUID().toString()
        val hashAlgorithmId = HashAlgorithm.getRandom()

        val hash = UserLoginInfoService.generatePasswordHash(
            password = password,
            salt = salt,
            hashAlgorithm = hashAlgorithmId
        )

        val isValid = UserLoginInfoService.checkValidPassword(
            password = password,
            userLoginInfo = UserLoginInfo.from(
                userId = UUID.randomUUID(),
                email = Email("${UUID.randomUUID()}@example.com"),
                passwordHash = hash,
                passwordSalt = salt,
                hashAlgorithmId = hashAlgorithmId,
                emailValidationStatus = EmailValidationStatus.NOT_CONFIRMED,
            ),
        )

        assert(isValid)
    }
}