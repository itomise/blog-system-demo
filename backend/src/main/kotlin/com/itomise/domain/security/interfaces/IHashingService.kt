package com.itomise.com.itomise.domain.security.interfaces

import com.itomise.com.itomise.domain.security.vo.SaltedHash

interface IHashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}