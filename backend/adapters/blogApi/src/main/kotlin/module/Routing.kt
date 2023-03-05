package com.itomise.blogApi.module

import com.itomise.blogApi.controller.post.postGet
import com.itomise.blogApi.controller.post.postGetList
import com.itomise.blogApi.controller.post.postSearch
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routing() {
    routing {
        route("/api/public") {
            postGetList()
            postGet()
            postSearch()
        }
    }
}
