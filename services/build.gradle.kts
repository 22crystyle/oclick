allprojects {
    group = "org.oclick.services"
    version = "unspecified"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "full-cycle-time")
    apply(plugin = "buildlogic.java-service-conventions")
}