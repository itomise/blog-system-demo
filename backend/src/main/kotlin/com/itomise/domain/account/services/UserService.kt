package com.itomise.com.itomise.domain.account.services

import com.itomise.com.itomise.domain.account.entities.User
import com.itomise.com.itomise.domain.account.interfaces.IUserService
import com.itomise.com.itomise.domain.account.vo.AccountOperationType
import com.itomise.com.itomise.domain.account.vo.UserId
import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.interfaces.INestedJwtTokenService
import com.itomise.com.itomise.domain.security.vo.HashAlgorithm
import com.itomise.com.itomise.domain.security.vo.SaltedHash
import com.itomise.com.itomise.module.envConfig
import com.itomise.com.itomise.util.getKoinInstance
import com.nimbusds.jose.jwk.JWKSet
import java.net.URL
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDateTime
import java.util.*

class UserService : IUserService {
    private val hashingService = getKoinInstance<IHashingService>()
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
        if (user.loginInfo == null) return false

        return hashingService.verifySaltedHash(
            value = password,
            saltedHash = SaltedHash(
                hash = user.loginInfo.passwordHash,
                salt = user.loginInfo.passwordSalt,
                algorithm = HashAlgorithm.get(user.loginInfo.userHashAlgorithmId.value)
            )
        )
    }

    override fun generateActivationToken(user: User): String {
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(envConfig.jwt.privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

        return nestedJwtTokenService.generate(
            signatureKey = privateKey,
            encryptionKey = envConfig.jwt.encryptionKey.toByteArray(),
            claims = mapOf(
                "userId" to user.id.value.toString(),
                "operationType" to AccountOperationType.ACTIVATE.value,
                "expires" to LocalDateTime.now().plusMinutes(2).toString()
            )
        )
    }

    override fun getUserIdFromActivationTokenOrNull(token: String): UserId {
        val publicKeys = JWKSet.load(URL("${envConfig.jwt.issuer}/.well-known/jwks.json"))
        val publicKey = publicKeys.getKeyByKeyId(envConfig.jwt.publicKeyId).toPublicJWK().toRSAKey()

        val jwtClaims = nestedJwtTokenService.verify(
            token = token,
            publicKey = publicKey,
            encryptionKey = envConfig.jwt.encryptionKey.toByteArray()
        ) ?: throw IllegalArgumentException("tokenが不正です。")

        val operationType = AccountOperationType.get(jwtClaims.getClaim("operationType").toString())

        if (operationType != AccountOperationType.ACTIVATE) {
            throw IllegalArgumentException("tokenが不正です。")
        }

        val expires = try {
            LocalDateTime.parse(jwtClaims.getClaim("expires").toString())
        } catch (e: Exception) {
            throw IllegalArgumentException("tokenが不正です。")
        }

        if (expires < LocalDateTime.now()) {
            throw IllegalArgumentException("tokenの有効期限が切れています。")
        }
        
        return UserId(UUID.fromString(jwtClaims.getClaim("userId").toString()))
    }
}