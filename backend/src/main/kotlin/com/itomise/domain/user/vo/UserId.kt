package com.itomise.com.itomise.domain.user.vo

data class UserId(val value: String) {
    companion object {
        private const val MIN_VALUE = 3
        private const val MAX_VALUE = 255
        private const val PATTERN = "^[a-zA-Z0-9.?/-]"
    }

    init {
        require(value.length >= MIN_VALUE) { "UserId は3文字以上である必要があります。" }
        require(value.length <= MAX_VALUE) { "UserId は255文字以下である必要があります。" }
        require(Regex(PATTERN).containsMatchIn(value)) { "不正な UserId です。" }
    }
}