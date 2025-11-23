dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

group = "org.oclick.platform.eureka"
version = "unspecified"

tasks.test {
    useJUnitPlatform()
}