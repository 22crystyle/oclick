plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "OClick"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}

include("shell")

include(
    "libs",
    "libs:shared",
    "libs:keycloak-provider",
    "libs:provider-contracts",
    "libs:provider-contracts:jobboard-api",
    "libs:provider-contracts:llm-api",
    "libs:provider-contracts:vectordb-api"
)

include(
    "services",
    "services:ai-service",
    "services:user-profile",
    "services:vacancy-service",
    "services:workflow-orchestrator"
)

include(
    "platform",
    "platform:api-gateway",
    "platform:eureka-server",
    "platform:config-server"
)

include(
    "providers",

    "providers:jobboards",
    "providers:jobboards:hh-provider",

    "providers:llm-backends",
    "providers:llm-backends:ollama-provider",

    "providers:vectordb",
    "providers:vectordb:qdrant-provider"
)