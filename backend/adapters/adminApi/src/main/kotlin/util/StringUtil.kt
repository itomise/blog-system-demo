package com.itomise.adminApi.util

fun removeHtmlTagFromString(value: String): String {
    return value.replace(Regex("<.*?>"), " ")
}