package com.itomise.com.itomise.domain.user

import com.itomise.com.itomise.domain.common.vo.Email
import com.itomise.com.itomise.domain.user.vo.UserId

class User private constructor(
    val id: UserId,
    val name: String,
    val email: Email
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
        name = name,
        email = this.email
    )

    companion object {
        fun create(id: UserId, name: String, email: Email) = User(
            id = id,
            name = name,
            email = email
        )
    }
}
