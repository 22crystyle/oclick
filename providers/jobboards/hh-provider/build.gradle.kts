group = "org.oclick.providers.jobboards.hh"
version = "unspecified"

tasks.withType<Jar> {
    manifest {
        attributes["Plugin-Id"] = "hh"
        attributes["Plugin-Version"] = project.version
        attributes["Plugin-Provider"] = "org.oclick"
    }
}