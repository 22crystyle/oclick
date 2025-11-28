group = "org.oclick.platform.gateway"
version = "unspecified"

dependencies {
    implementation(libs.spring.cloud.starter.gateway.webflux)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.cloud.starter.loadbalancer)
    implementation(libs.resilience4j)
}

configurations.implementation {
    exclude(module = "spring-webmvc")
    exclude(module = "spring-boot-starter-tomcat")
}
