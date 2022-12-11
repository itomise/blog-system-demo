package com.itomise.com.itomise.util.security.hashing

import io.ktor.util.*
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256HashingService : HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = hex(salt)

        val hash = DigestUtils.sha256Hex("$salt$value")

        return SaltedHash(
            salt = saltAsHex,
            hash = hash
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}