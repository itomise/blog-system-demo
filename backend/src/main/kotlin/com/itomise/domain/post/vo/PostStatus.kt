package com.itomise.com.itomise.domain.post.vo

enum class PostStatus(val value: Int) {
    DRAFT(1),
    PUBLISHED(2),
    ARCHIVED(3);

    companion object {
        fun get(value: Int) =
            values().find { it.value == value } ?: throw IllegalArgumentException("不正な PostStatus です。 value: $value")
    }
}