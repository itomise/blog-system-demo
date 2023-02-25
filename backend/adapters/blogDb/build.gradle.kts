plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

group = "com.itomise.blogDb"
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
    // orm
    implementation(libs.exposedCore)
    implementation(libs.exposedDao)
    implementation(libs.exposedJdbc)
    implementation(libs.exposedJavaTime)
    // injection
    implementation(libs.koinKtor)
    implementation(libs.koinLogger)
    // cp
    implementation(libs.hikariCp)
    // database
    implementation(libs.postgres)
    implementation(libs.googleCloudSqlPostgresSocketFactory)
    // redis
    implementation(libs.redisKreds)
    // core
    implementation(project(":core"))

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