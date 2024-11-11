plugins {
    java
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("fr.stardustenterprises.rust.importer") version("3.2.5")
}

toolkitMavenPublishing {
    artifactName.set("${rootProject.name}-${project.name}")
}

dependencies {
    rust(project(":rust"))
    implementation(project(":"))
}

rustImport {
    baseDir.set("natives")
    layout.set("flat")
}

tasks {

    jar {
        // Only bundle the native this target is for
        val split = project.name.split("-").toMutableSet()
        split.removeIf { word -> word == "natives" }
        val os = split.first()
        val arch = split.last()

        val fileExtension = when (os) {
            "windows" -> "dll"
            "linux" -> "so"
            "osx" -> "dylib"
            else -> throw IllegalArgumentException("Unknown OS: $os")
        }

        val fileName = (if (os != "windows") "lib" else "") + "${rootProject.name}-$arch.$fileExtension"

        logger.lifecycle("Including $fileName in JAR for ${project.name}")
        include("**/$fileName")
    }

}
