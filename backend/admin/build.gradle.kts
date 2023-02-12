val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jackson_version: String by project
val exposed_version: String by project
val h2_version: String by project
val koin_version: String by project
val postgresql_version: String by project
val hikaricp_version: String by project
val kreds_version: String by project
val commons_codec: String by project
val nimbus_jose_version: String by project
val sendgrid_version: String by project
val mockk_version: String by project
val fuel_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

group = "com.itomise.admin"
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
    // kotlin
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    // server
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")
    // auth
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("commons-codec:commons-codec:$commons_codec")
    // jwt
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbus_jose_version")
    // mail
    implementation("com.sendgrid:sendgrid-java:$sendgrid_version")
    // redis
    implementation("io.github.crackthecodeabhi:kreds:$kreds_version")
    // cp
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    // serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")
    // http client
    implementation("com.github.kittinunf.fuel:fuel:$fuel_version")
    implementation("com.github.kittinunf.fuel:fuel-jackson:$fuel_version")
    // logging
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    // orm
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    // injection
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    // database
    implementation("org.postgresql:postgresql:$postgresql_version")
    implementation("com.google.cloud.sql:postgres-socket-factory:1.7.2")
    // test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}