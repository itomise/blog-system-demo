package com.itomise.com.itomise.domain.account.services

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.AccountOperationType
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.account.vo.UserInternalLoginInfo
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.interfaces.IJwtTokenService
import com.itomise.com.itomise.domain.security.interfaces.INestedJwtTokenService
import com.itomise.com.itomise.domain.security.vo.HashAlgorithm
import com.itomise.com.itomise.domain.security.vo.SaltedHash
import com.itomise.com.itomise.domain.security.vo.TokenClaim
import com.itomise.com.itomise.module.envConfig
import com.itomise.com.itomise.module.jwtTokenConfig
import com.itomise.com.itomise.util.getKoinInstance
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDateTime
import java.util.*

class UserService : IUserService {
    private val hashingService = getKoinInstance<IHashingService>()
    private val jwtTokenService = getKoinInstance<IJwtTokenService>()
    private val nestedJwtTokenService = getKoinInstance<INestedJwtTokenService>()

    override suspend fun isDuplicateUser(allUsers: List<User>, user: User): Boolean {
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

    override fun isValidPassword(password: String, user: User): Boolean {
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

    override fun generateActivationToken(user: User): String {
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(envConfig.jwt.privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

        return jwtTokenService.generate(
            config = jwtTokenConfig,
            privateKey = privateKey,
            claims = arrayOf(
                TokenClaim("userId", user.id.value.toString()),
                TokenClaim("operationType", AccountOperationType.ACTIVATE.value),
                TokenClaim("expires", LocalDateTime.now().plusHours(24).toString()),
                TokenClaim("loginType", user.loginType.value.toString()) // internal の場合は null になっているため
            )
        )
    }

    override fun getUserIdFromActivationToken(token: String): UserId {
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

        return UserId(UUID.fromString(decodedJwt.getClaim("userId").asString()))
    }
}