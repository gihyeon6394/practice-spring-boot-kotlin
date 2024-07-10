import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion: String by extra { "3.3.1" }
val jacksonVersion: String by extra { "2.17.0" }
val springCloudVersion: String by extra { "2023.0.2" }
val querydslVersion: String by extra { "5.1.0" }


plugins {
    val springBootVersion = "3.3.1"
    val kotlinVersion = "2.0.0"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion

}

group = "com.practice"
version = "0.0.1-SNAPSHOT"

java {
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")

    implementation("com.mysql:mysql-connector-j:8.4.0")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")

    implementation("com.querydsl:querydsl-core:${querydslVersion}")
    implementation("com.querydsl:querydsl-jpa:${querydslVersion}")
    implementation("com.querydsl:querydsl-sql:${querydslVersion}")
    implementation("com.querydsl:querydsl-apt:${querydslVersion}")
    kapt("com.querydsl:querydsl-apt:${querydslVersion}:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation(kotlin("stdlib-jdk8"))

}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
