import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.deftu.dev/releases/")
        maven("https://maven.deftu.dev/snapshots/")
        maven("https://jitpack.io/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.architectury.dev/")
        maven("https://repo.essential.gg/repository/maven-public/")
    }
}

rootProject.name = extra["project.name"]?.toString() ?: throw MissingPropertyException("The project name was not configured!")

// Native code
include("rust")
listOf(
    "windows-x64",
    "windows-x86",
    "linux-x64",
    "linux-x86",
    "linux-arm",
    "linux-arm64",
    // "macos-x64",
    // "macos-arm64"
).forEach { target ->
    include("natives-$target")
    project(":natives-$target").apply {
        projectDir = file("natives/targets/$target").apply {
            if (!exists()) {
                mkdirs()
            }
        }

        buildFileName = "../../build.gradle.kts"
    }
}

include("image-awt")
