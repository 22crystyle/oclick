group = "org.oclick.platform.gateway"
version = "unspecified"

plugins {
    id("buildlogic.java-service-conventions")
}

dependencies {
    implementation(libs.spring.cloud.starter.gateway)
}