package com.itomise.admin.util

fun removeHtmlTagFromString(value: String): String {
    return value.replace(Regex("<.*?>"), " ")
}