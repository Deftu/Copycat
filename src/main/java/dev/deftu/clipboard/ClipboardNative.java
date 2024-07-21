package dev.deftu.clipboard;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class ClipboardNative {

    private ClipboardNative() {
    }

    @Nullable
    @ApiStatus.Internal
    public static native String getString();

    @ApiStatus.Internal
    public static native boolean setString(String value);

    @ApiStatus.Internal
    public static native ClipboardImage getImage();

    @ApiStatus.Internal
    public static native boolean setImage(ClipboardImage image);

    @ApiStatus.Internal
    public static native boolean clear();

    @ApiStatus.Internal
    public static String getDefaultNativeFileName() {
        String fileName = modifyNativeFileNameForOs("copycat-%s");
        String architecturalSuffix = getArchitecturalSuffix();
        return String.format(fileName, architecturalSuffix);
    }

    /**
     * @param fileName the name of the native file without the extension or possible prefix
     * @return the name of the native file with the correct extension and prefix for the current OS
     */
    private static String modifyNativeFileNameForOs(String fileName) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return fileName + ".dll";
        } else if (osName.contains("mac")) {
            return "lib" + fileName + ".dylib";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return "lib" + fileName + ".so";
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }

    /**
     * @return the suffix for the architecture of the current OS. May return x86, x64 or arm64.
     */
    private static String getArchitecturalSuffix() {
        String osArch = System.getProperty("os.arch");
        if (osArch.contains("64")) {
            return "x64";
        } else if (osArch.contains("86")) {
            return "x86";
        } else if (osArch.contains("arm64")) {
            return "arm64";
        } else {
            throw new UnsupportedOperationException("Unsupported architecture: " + osArch);
        }
    }

}
