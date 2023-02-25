package com.itomise.core.lib.fuel

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.itomise.core.lib.jackson.JacksonUtil

inline fun <reified T : Any> Request.responseCustomObject() = response(jacksonDeserializerOf<T>(JacksonUtil.mapper))