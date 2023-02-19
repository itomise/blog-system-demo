val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jackson_version: String by project
val exposed_version: String by project
val koin_version: String by project
val postgresql_version: String by project
val hikaricp_version: String by project
val commons_codec: String by project
val mockk_version: String by project
val fuel_version: String by project
val cloud_sql_postgres_factory_version: String by project
val logstash_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

group = "com.itomise.shared"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // server
    implementation(libs.ktorNetty)
    implementation(libs.ktorCors)
    implementation(libs.ktorStatusPages)
    implementation(libs.ktorForwardedHeader)
    // cp
    implementation(libs.hikariCp)
    // serialization
    implementation(libs.ktorContentNegotiation)
    implementation(libs.ktorJackson)
    implementation(libs.jackson)
    // http client
    implementation(libs.fuel)
    implementation(libs.fuelJackson)
    // logging
    implementation(libs.ktorCallLogging)
    implementation(libs.logback)
    implementation(libs.logstashLogbackEncoder)
    // orm
    implementation(libs.exposedCore)
    implementation(libs.exposedDao)
    implementation(libs.exposedJdbc)
    implementation(libs.exposedJavaTime)
    // injection
    implementation(libs.koinKtor)
    implementation(libs.koinLogger)
    // database
    implementation(libs.postgres)
    implementation(libs.googleCloudSqlPostgresSocketFactory)
    // test
    implementation(libs.ktorServerTests)
    implementation(libs.kotlinTestJunit)
    implementation(libs.koinTest)
    implementation(libs.koinTestJunit4)
    implementation(libs.mockk)
}

tasks.test {
    maxHeapSize = "2g"

    maxParallelForks = 5
}