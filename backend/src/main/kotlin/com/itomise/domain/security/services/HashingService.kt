package com.itomise.com.itomise.domain.security.services

import com.itomise.com.itomise.domain.security.interfaces.IHashingService
import com.itomise.com.itomise.domain.security.vo.HashAlgorithm
import com.itomise.com.itomise.domain.security.vo.SaltedHash
import io.ktor.util.*
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class HashingService : IHashingService {
    private fun hash(value: String, algorithm: HashAlgorithm): String {
        return when (algorithm) {
            HashAlgorithm.SHA256 -> DigestUtils.sha3_256Hex(value)
            HashAlgorithm.SHA384 -> DigestUtils.sha3_384Hex(value)
            HashAlgorithm.SHA512 -> DigestUtils.sha3_512Hex(value)
        }
    }

    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = hex(salt)

        val algorithm = HashAlgorithm.getRandom()

        val hash = hash("$saltAsHex$value", algorithm)

        return SaltedHash(
            salt = saltAsHex,
            hash = hash,
            algorithm = algorithm
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return hash(saltedHash.salt + value, saltedHash.algorithm) == saltedHash.hash
    }
}