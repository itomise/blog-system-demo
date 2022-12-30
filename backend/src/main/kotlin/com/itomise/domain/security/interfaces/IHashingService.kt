package com.itomise.com.itomise.domain.security.interfaces

import com.itomise.com.itomise.domain.security.vo.SaltedHash

interface IHashingService {
    fun generateSaltedHash(value: String): SaltedHash
    fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean
}