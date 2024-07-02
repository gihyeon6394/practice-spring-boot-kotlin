import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion: String by extra { "3.2.5" }
val jacksonVersion: String by extra { "2.17.0" }
val springCloudVersion: String by extra { "2023.0.2" }
val querydslVersion: String by extra { "5.1.0" }


plugins {
    val springBootVersion = "3.2.5"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("kapt") version "2.0.0"

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

dependencies {
    kapt("groupId:artifactId:version")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.mysql:mysql-connector-j:8.4.0")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")

    implementation("com.querydsl:querydsl-core:${querydslVersion}")
    implementation("com.querydsl:querydsl-jpa:${querydslVersion}")
    implementation("com.querydsl:querydsl-sql:${querydslVersion}")
    implementation("com.querydsl:querydsl-apt:${querydslVersion}")
    kapt("com.querydsl:querydsl-apt:${querydslVersion}:jpa")

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
