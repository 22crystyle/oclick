plugins {
    `java-library`
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.pf4j:pf4j:3.13.0")
    annotationProcessor("org.pf4j:pf4j:3.13.0")
    compileOnly("org.pf4j:pf4j:3.13.0") {
        exclude(group = "org.slf4j")
    }

    implementation("org.springframework.boot:spring-boot-starter")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}