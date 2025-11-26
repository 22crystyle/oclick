allprojects {
    group = "org.oclick.providers"
    version = "unspecified"
}

val providerTypes = listOf("jobboards", "llm-backends", "vectordb")

val copyTasks = providerTypes.map { providerType ->
    tasks.register<Copy>("register${providerType.replaceFirstChar { it.uppercase() }}Plugins") {
        group = "custom"
        description = "Assembles and copies all $providerType plugins to the plugins directory."

        val projects = rootProject.subprojects.filter { it.path.startsWith(":providers:$providerType:") }
        val jarTasks = projects.map { it.tasks.named("jar", Jar::class.java) }

        dependsOn(jarTasks)

        from(jarTasks.map { it.flatMap { it.archiveFile } })
        into(rootProject.projectDir.resolve("plugins/$providerType"))
    }
}

tasks.register("registerPlugins") {
    group = "custom"
    description = "Assembles and copies all provider plugins to the plugins directory."
    dependsOn(copyTasks)
}