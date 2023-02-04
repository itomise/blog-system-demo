package com.itomise.com.itomise.util

fun removeHtmlTagFromString(value: String): String {
    return value.replace(Regex("<.*?>"), " ")
}