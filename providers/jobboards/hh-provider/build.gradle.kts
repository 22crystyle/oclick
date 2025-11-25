plugins {
    id("java")
}

group = "org.oclick.providers.jobboards.hh"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":libs:provider-contracts:jobboard-api"))
    implementation(project(":libs:shared"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}