plugins {
    java
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
}

dependencies {
    implementation(project(":"))
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
    }

}
