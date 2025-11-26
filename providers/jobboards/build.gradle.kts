allprojects {
    group = "org.oclick.providers.jobboards"
    version = "unspecified"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "buildlogic.spring-plugin-conventions")

    dependencies {
        add("implementation", project(":libs:provider-contracts:jobboard-api"))
        add("implementation", project(":libs:shared"))
    }
}