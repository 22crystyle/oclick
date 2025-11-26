plugins {
    id("java")
}

group = "org.oclick.libs.contracts.llm-api"
version = "unspecified"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}