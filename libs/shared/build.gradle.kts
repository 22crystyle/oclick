plugins {
    id("java")
}

group = "org.oclick.libs.shared"
version = "unspecified"

dependencies {
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.web)
}

tasks.test {
    useJUnitPlatform()
}