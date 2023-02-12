package com.itomise.admin.domain.security.interfaces

import com.itomise.admin.domain.security.vo.SaltedHash

interface IHashingService {
    fun generateSaltedHash(value: String): SaltedHash
    fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean
}