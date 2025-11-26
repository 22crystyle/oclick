allprojects {
    group = "org.oclick.providers.llm-backends"
    version = "unspecified"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "buildlogic.spring-plugin-conventions")
}