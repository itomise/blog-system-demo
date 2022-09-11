package com.itomise

import com.itomise.infrastructure.DataBaseFactory.allDaoList
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SchemaUtils

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
            allDaoList.forEach {
                SchemaUtils.drop(it)
                SchemaUtils.create(it)
            }
        }
    }
}