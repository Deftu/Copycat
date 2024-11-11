import groovy.lang.MissingPropertyException
import org.gradle.internal.os.OperatingSystem

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
mapOf(
    "windows" to listOf("x64", "x86"),
    "linux" to listOf("x64", "x86", "arm", "arm64"),
    "osx" to listOf("x64", "arm64")
).forEach { (key, arches) ->

    if (OperatingSystem.forName(key) != OperatingSystem.current())
        return@forEach

    arches.forEach { arch ->
        val target = "$key-$arch"

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
}

include("image-awt")
