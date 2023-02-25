package com.itomise.admin.domain.user.services

import com.itomise.admin.domain.user.vo.AccountOperationType
import com.itomise.admin.domain.user.vo.UserId
import com.itomise.admin.domain.user.vo.UserInternalLoginInfo
import com.itomise.admin.domain.security.services.HashingService
import com.itomise.admin.domain.security.services.JwtTokenService
import com.itomise.admin.domain.security.vo.HashAlgorithm
import com.itomise.admin.domain.security.vo.SaltedHash
import com.itomise.admin.domain.security.vo.TokenClaim
import com.itomise.admin.domain.user.entities.User
import com.itomise.admin.module.adminEnvConfig
import com.itomise.admin.module.jwtTokenConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
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

    fun generateActivationToken(user: User): String {
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminEnvConfig.jwt.privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

        return jwtTokenService.generate(
            config = jwtTokenConfig,
            privateKey = privateKey,
            claims = arrayOf(
                TokenClaim("userId", user.id.toString()),
                TokenClaim("operationType", AccountOperationType.ACTIVATE.value),
                TokenClaim("expires", LocalDateTime.now().plusHours(24).toString()),
                TokenClaim("loginType", user.loginType.value.toString()) // internal の場合は null になっているため
            )
        )
    }

    fun getUserIdFromActivationToken(token: String): UserId {
        val decodedJwt = jwtTokenService.verify(
            config = jwtTokenConfig,
            token = token
        ) ?: throw IllegalArgumentException("tokenが不正です。")

        val operationType = AccountOperationType.get(decodedJwt.getClaim("operationType").asString())

        if (operationType != AccountOperationType.ACTIVATE) {
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