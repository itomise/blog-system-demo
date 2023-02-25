package com.itomise.core.domain.security.services

import com.itomise.core.domain.security.vo.HashAlgorithm
import com.itomise.core.domain.security.vo.SaltedHash
import io.ktor.util.*
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class HashingService {
    private val saltLength = 32
    private val stretchingCount = 1500

    private fun hash(value: String, algorithm: HashAlgorithm): String {
        return when (algorithm) {
            HashAlgorithm.SHA256 -> DigestUtils.sha3_256Hex(value)
            HashAlgorithm.SHA384 -> DigestUtils.sha3_384Hex(value)
            HashAlgorithm.SHA512 -> DigestUtils.sha3_512Hex(value)
        }
    }

    fun generateSaltedHash(value: String): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = hex(salt)

        val algorithm = HashAlgorithm.getRandom()

        var hash = hash("$saltAsHex$value", algorithm)

        (1..stretchingCount).forEach { _ ->
            hash = hash(hash, algorithm)
        }

        return SaltedHash(
            salt = saltAsHex,
            hash = hash,
            algorithm = algorithm
        )
    }

    fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean {
        var hash = hash(saltedHash.salt + value, saltedHash.algorithm)

        (1..stretchingCount).forEach { _ ->
            hash = hash(hash, saltedHash.algorithm)
        }

        return hash == saltedHash.hash
    }
}