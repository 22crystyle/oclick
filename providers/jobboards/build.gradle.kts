allprojects {
    group = "org.oclick.providers.jobboards"
    version = "unspecified"
}

subprojects {
    apply(plugin = "java")

    dependencies {
        add("implementation", project(":libs:provider-contracts:jobboard-api"))
        add("implementation", project(":libs:shared"))
    }
}