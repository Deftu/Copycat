import org.gradle.internal.os.OperatingSystem

plugins {
    java
    val dgtVersion = "2.19.1"
    id("dev.deftu.gradle.tools") version (dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version (dgtVersion)
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")

    rootProject.subprojects.filter { project ->
        project.name.startsWith("natives-")
    }.forEach(::testImplementation)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {

    test {
        useJUnitPlatform()
        ignoreFailures = System.getenv("CI") == "true"
    }

    setOf(
        "DeftuReleases",
        "DeftuSnapshots"
    ).forEach { repository ->
        register("publishNativesTo${repository}Repository") {
            group = "publishing"

            val osName = OperatingSystem.current().familyName.lowercase().replace(" ", "")
            mapOf(
                "windows" to listOf("x64", "x86"),
                "linux" to listOf("x64", "x86", "arm", "arm64"),
                "osx" to listOf("x64", "arm64")
            )[osName]?.forEach { arch ->
                val target = "$osName-$arch"

                dependsOn(":natives-$target:publishAllPublicationsTo${repository}Repository")
            }
        }
    }

}