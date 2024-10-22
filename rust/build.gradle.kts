plugins {
    id("fr.stardustenterprises.rust.wrapper") version("3.2.5")
}

rust {
    release.set(true)
    command.set("cross")
    cargoInstallTargets.set(true)

    // Windows x64 and x86
    targets += target("x86_64-pc-windows-msvc", "${rootProject.name}-x64.dll")
    targets += target("i686-pc-windows-msvc", "${rootProject.name}-x86.dll")

    // Linux x64, x86, arm and arm64
    targets += target("x86_64-unknown-linux-gnu", "lib${rootProject.name}-x64.so")
    targets += target("i686-unknown-linux-gnu", "lib${rootProject.name}-x86.so")
    targets += target("arm-unknown-linux-gnueabihf", "lib${rootProject.name}-arm.so")
    targets += target("aarch64-unknown-linux-gnu", "lib${rootProject.name}-arm64.so")

    // MacOS x64 and arm64
    // targets += target("x86_64-apple-darwin", "lib${rootProject.name}-x64.dylib")
    // targets += target("aarch64-apple-darwin", "lib${rootProject.name}-arm64.dylib")
}
