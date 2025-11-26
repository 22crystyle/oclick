group = "org.oclick.services.vacancy-processor"
version = "unspecified"

dependencies {
    implementation(project(":libs:provider-contracts:jobboard-api"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.pf4j)
    implementation(libs.pf4j.spring)
}