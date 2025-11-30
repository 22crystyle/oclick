group = "org.oclick.shell"
version = "unspecified"

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
    gradlePluginPortal()
}

dependencies {
    implementation(libs.spring.shell)
    implementation(libs.jline)

    implementation(libs.spring.boot.starter.web)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}