plugins {
    id("java")
}

group = "org.oclick.libs.contracts.jobboard-api"
version = "unspecified"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":libs:shared"))
}

tasks.test {
    useJUnitPlatform()
}