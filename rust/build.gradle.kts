import org.gradle.internal.os.OperatingSystem

plugins {
    id("fr.stardustenterprises.rust.wrapper") version("3.2.5")
}

rust {
    release.set(true)
    cargoInstallTargets.set(true)

    val osName = OperatingSystem.current().name.lowercase()

    val targetsMap = mapOf(
        "windows" to listOf(
            Pair("x64", "x86_64-pc-windows-msvc"),
            Pair("x86", "i686-pc-windows-msvc"),
        ),
        "linux" to listOf(
            Pair("x64", "x86_64-unknown-linux-gnu"),
            Pair("x86", "i686-unknown-linux-gnu"),
            Pair("arm", "arm-unknown-linux-gnueabihf"),
            Pair("arm64", "aarch64-unknown-linux-gnu"),
        ),
        "osx" to listOf(
            Pair("x64", "x86_64-apple-darwin"),
            Pair("arm64", "aarch64-apple-darwin"),
        ),
    )

    val extensionMap = mapOf(
        "windows" to "dll",
        "linux" to "so",
        "osx" to "dylib",
    )

    targetsMap[osName]?.forEach { (arch, targetName) ->
        targets += target(targetName, "lib${rootProject.name}-${arch}.${extensionMap[osName]}")
    }
}