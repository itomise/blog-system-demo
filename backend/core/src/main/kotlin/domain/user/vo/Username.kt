package com.itomise.core.domain.user.vo

data class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "ユーザー名は1文字以上である必要があります。" }
    }
}
