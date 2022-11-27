package com.itomise.com.itomise.domain.user

import com.itomise.com.itomise.domain.common.vo.Email
import java.util.*

class User private constructor(
    val id: UUID,
    val email: Email,
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "ユーザー名は1文字以上である必要があります。" }
    }

    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return this.id == other.id
        }
        return false
    }

    fun changeName(name: String) = User(
        id = this.id,
        email = this.email,
        name = name,
    )

    companion object {
        fun create(id: UUID, email: Email, name: String) = User(
            id = id,
            email = email,
            name = name,
        )
    }
}
