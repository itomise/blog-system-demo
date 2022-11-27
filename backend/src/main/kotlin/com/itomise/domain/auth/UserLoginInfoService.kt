package com.itomise.com.itomise.domain.auth

import com.itomise.com.itomise.domain.auth.interfaces.IUserLoginInfoRepository
import com.itomise.com.itomise.util.getKoinInstance
import java.math.BigInteger
import java.security.MessageDigest

class UserLoginInfoService {
    companion object {
        private val userLoginInfoRepository = getKoinInstance<IUserLoginInfoRepository>()
        private val hashAlgorithmMap = mapOf(
            HashAlgorithm.FIRST to "SHA3-512",
            HashAlgorithm.SECOND to "SHA3-256"
        )
        private const val STRETCHING_COUNT = 1500

        suspend fun isDuplicateUser(userLoginInfo: UserLoginInfo): Boolean {
            return userLoginInfoRepository.findByEmail(userLoginInfo.email) != null
        }

        fun generatePasswordHash(password: String, salt: String, hashAlgorithm: HashAlgorithm): String {
            val md = MessageDigest
                .getInstance(hashAlgorithmMap[hashAlgorithm])

            var digest = md.digest("$password$salt".toByteArray())
            repeat(STRETCHING_COUNT) {
                digest = md.digest(digest)
            }

            val no = BigInteger(1, digest)

            return no.toString(16)
        }

        fun checkValidPassword(
            password: String,
            userLoginInfo: UserLoginInfo,
        ): Boolean {
            val hash = this.generatePasswordHash(password, userLoginInfo.passwordSalt, userLoginInfo.hashAlgorithmId)
            return hash == userLoginInfo.passwordHash
        }
    }
}