plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.exposed:exposed-core:0.50.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.50.0")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.50.0")

    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    implementation("io.lettuce:lettuce-core:6.5.1.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Test>() {
    enabled = false
}
