
val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project
val jackson_version = "2.18.1"
val graphQLKotlinVersion = "8.2.1"


plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    id("com.expediagroup.graphql") version  "8.2.1"// (2)
}

group = "com.example"
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
    implementation("io.insert-koin:koin-ktor:3.5.6")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")
//    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.mongodb:mongodb-driver-core:$mongo_version")
    implementation("org.mongodb:mongodb-driver-sync:$mongo_version")
    implementation("org.mongodb:bson:$mongo_version")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-resources-jvm")
    implementation("io.ktor:ktor-server-csrf-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-apache-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.insert-koin:koin-test:3.5.6")
    testImplementation("io.insert-koin:koin-test-junit5:3.5.6")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
//    implementation("com.expediagroup:graphql-kotlin-server:$graphQLKotlinVersion")
    implementation("com.expediagroup:graphql-kotlin-schema-generator:$graphQLKotlinVersion")
    implementation("com.expediagroup", "graphql-kotlin-ktor-server", graphQLKotlinVersion)
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
    implementation("io.pinecone:pinecone-client:3.0.0")
    implementation("com.cohere:cohere-java:1.3.2")
    implementation("com.google.protobuf:protobuf-kotlin:4.28.3")
    implementation("org.apache.lucene:lucene-analysis-common:9.8.0")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.1")
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(17)
}