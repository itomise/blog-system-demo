package com.itomise.core.domain.post.vo

enum class PostStatus(val value: Int) {
    UN_PUBLISHED(1),
    PUBLISH(2);

    companion object {
        fun get(value: Int) =
            values().find { it.value == value } ?: throw IllegalArgumentException("不正な PostStatus です。 value: $value")
    }
}