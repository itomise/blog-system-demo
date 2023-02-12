package com.itomise.admin.domain.account.vo

data class Email(val value: String) {
    private val emailPattern = "[a-zA-Z0-9._+-]+@[a-zA-Z0-9._+-]+\\.+[a-z]+"

    init {
        require(Regex(emailPattern).matches(input = value)) { "メールアドレスの形式が不正です。" }
    }
}
