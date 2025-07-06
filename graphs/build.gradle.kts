plugins {
    kotlin("jvm") version "1.9.20"
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.4"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-html-builder:2.3.7")

    // RocketMQ
    implementation("org.apache.rocketmq:rocketmq-client:4.9.4")
    implementation("org.apache.rocketmq:rocketmq-common:4.9.4")
    implementation("org.apache.rocketmq:rocketmq-remoting:4.9.4")
    implementation("org.apache.rocketmq:rocketmq-spring-boot-starter:2.2.3")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "ch.qos.logback")
        exclude(group = "org.apache.logging.log4j")
    }
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "ch.qos.logback")
        exclude(group = "org.apache.logging.log4j")
    }

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-core:1.2.12")
    implementation("ch.qos.logback:logback-classic:1.2.12")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

application {
    mainClass.set("org.example.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}