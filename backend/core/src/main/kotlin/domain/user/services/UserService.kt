package com.itomise.core.domain.user.services

import com.itomise.core.domain.security.services.HashingService
import com.itomise.core.domain.security.services.JwtTokenService
import com.itomise.core.domain.security.vo.HashAlgorithm
import com.itomise.core.domain.security.vo.SaltedHash
import com.itomise.core.domain.security.vo.TokenClaim
import com.itomise.core.domain.security.vo.TokenConfig
import com.itomise.core.domain.user.entities.User
import com.itomise.core.domain.user.vo.UserId
import com.itomise.core.domain.user.vo.UserInternalLoginInfo
import com.itomise.core.domain.user.vo.UserOperationType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.security.PrivateKey
import java.security.PublicKey
import java.time.LocalDateTime
import java.util.*

class UserService : KoinComponent {
    private val hashingService by inject<HashingService>()
    private val jwtTokenService by inject<JwtTokenService>()

    suspend fun isDuplicateUser(allUsers: List<User>, user: User): Boolean {
        val targetUser = allUsers.find {
            val isDuplicateUserId = it.id == user.id
            val isDuplicateEmail = it.email == user.email

            if (!isDuplicateUserId && isDuplicateEmail) {
                throw IllegalStateException("不正なユーザーが存在します。")
            }
            if (isDuplicateUserId && !isDuplicateEmail) {
                throw IllegalStateException("不正なユーザーが存在します。")
            }

            isDuplicateUserId
        }

        return targetUser != null
    }

    fun isValidPassword(password: String, user: User): Boolean {
        if (user.loginInfo == null || user.loginInfo !is UserInternalLoginInfo) return false

        return hashingService.verifySaltedHash(
            value = password,
            saltedHash = SaltedHash(
                hash = user.loginInfo.passwordHash,
                salt = user.loginInfo.passwordSalt,
                algorithm = HashAlgorithm.get(user.loginInfo.hashAlgorithmId.value)
            )
        )
    }

    fun generateActivationToken(
        user: User,
        tokenConfig: TokenConfig,
        publicKey: PublicKey,
        privateKey: PrivateKey
    ): String {
        return jwtTokenService.generate(
            config = tokenConfig,
            privateKey = privateKey,
            publicKey = publicKey,
            claims = arrayOf(
                TokenClaim("userId", user.id.toString()),
                TokenClaim("operationType", UserOperationType.ACTIVATE.value),
                TokenClaim("expires", LocalDateTime.now().plusHours(24).toString()),
                TokenClaim("loginType", user.loginType.value.toString()) // internal の場合は null になっているため
            )
        )
    }

    fun getUserIdFromActivationToken(token: String, tokenConfig: TokenConfig, publicKey: PublicKey): UserId {
        val decodedJwt = jwtTokenService.verify(
            config = tokenConfig,
            token = token,
            publicKey = publicKey
        ) ?: throw IllegalArgumentException("tokenが不正です。")

        val operationType = UserOperationType.get(decodedJwt.getClaim("operationType").asString())

        if (operationType != UserOperationType.ACTIVATE) {
            throw IllegalArgumentException("tokenが不正です。")
        }

        val expires = try {
            LocalDateTime.parse(decodedJwt.getClaim("expires").asString())
        } catch (e: Exception) {
            throw IllegalArgumentException("tokenが不正です。")
        }

        if (expires < LocalDateTime.now()) {
            throw IllegalArgumentException("tokenの有効期限が切れています。")
        }

        return UUID.fromString(decodedJwt.getClaim("userId").asString())
    }
}