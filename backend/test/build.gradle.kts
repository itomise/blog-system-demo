plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

group = "com.itomise.test"
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
    // serialization
    implementation(libs.ktorContentNegotiation)
    implementation(libs.ktorJackson)
    implementation(libs.jackson)
    // jwt
    implementation(libs.ktorAuthJwt)
    implementation(libs.nimbusJoseJwt)
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
    implementation(project(":core"))
    implementation(project(":adapters:blogDb"))
    implementation(project(":adapters:eventBus"))

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