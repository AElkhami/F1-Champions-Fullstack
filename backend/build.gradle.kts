plugins {
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.elkhami"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

val resilience4jVersion = "2.3.0"

dependencies {
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Resilience4j
    implementation("io.github.resilience4j:resilience4j-spring-boot3:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-reactor:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-micrometer:$resilience4jVersion")

    // Micrometer
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-observation")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // PostgresSql
    runtimeOnly("org.postgresql:postgresql")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")

    // Mockk
    testImplementation("io.mockk:mockk:1.14.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.2.1")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("build") {
    dependsOn("format")
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}

tasks.register("format") {
    group = "formatting"
    description = "Applies code formatting using Spotless"
    dependsOn("spotlessApply")
}
