allprojects {
    group = "org.oclick.providers.vectordb"
    version = "unspecified"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "buildlogic.spring-plugin-conventions")
}