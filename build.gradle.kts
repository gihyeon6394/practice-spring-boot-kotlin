import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion: String by extra { "3.2.5" }


plugins {
    val springBootVersion = "3.2.5"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"

}

group = "com.practice"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")

    implementation("com.mysql:mysql-connector-j:8.4.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("org.springframework.data:spring-data-jpa:${springBootVersion}")

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
