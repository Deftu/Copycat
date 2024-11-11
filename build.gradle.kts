plugins {
    java
    val dgtVersion = "2.4.1"
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

}
