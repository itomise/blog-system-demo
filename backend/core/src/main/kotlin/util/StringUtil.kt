package com.itomise.core.util

fun removeHtmlTagFromString(value: String): String {
    return value.replace(Regex("<.*?>"), " ").trim()
}