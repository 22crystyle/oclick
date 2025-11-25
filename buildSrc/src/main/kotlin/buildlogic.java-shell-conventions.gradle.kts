import gradle.kotlin.dsl.accessors._9d3e45433fbb9626835e83aa6034b0bc.dependencyManagement

plugins {
    `java-library`
    id("io.freefair.lombok")
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.shell:spring-shell-dependencies:3.4.1")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.shell:spring-shell-starter")
    implementation("org.springframework:spring-webmvc:6.2.14")
    implementation("org.springframework.boot:spring-boot-starter-json")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
