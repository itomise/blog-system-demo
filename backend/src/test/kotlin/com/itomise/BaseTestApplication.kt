package com.itomise

import io.ktor.server.config.*
import io.ktor.server.testing.*

class BaseTestApplication() {
    companion object {
        fun baseTestApplication(
            block: suspend ApplicationTestBuilder.() -> Unit
        ) {
            testApplication {
                environment {
                    config = ApplicationConfig("application.local.conf")
                }

                // 全てのテーブルを初期化
                refreshDatabase()

                block()
            }
        }

        private fun refreshDatabase() {
            TODO("後で実装")
        }
    }
}