package dev.deftu.clipboard;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * An easily extensible class providing an easy means of interacting with the system clipboard.
 */
public class Clipboard {

    private static boolean nativeLoaded = false;
    private static Clipboard instance = null; // lazy-loaded

    /**
     * Constructs a new instance of the clipboard.
     * <p>
     * If the native library has not already been loaded, it will attempt to load the default native library provided by the library.
     */
    public Clipboard() {
        if (!nativeLoaded) {
            try {
                attemptLoadDefaultNative();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    /**
     * @return The string currently stored in the clipboard, or null if the clipboard contains a non-text value or is empty.
     */
    @Nullable
    public String getString() {
        return ClipboardNative.getString();
    }

    /**
     * Sets the clipboard to the specified string.
     *
     * @param value The string to set the clipboard to.
     * @return True if the operation was successful, false otherwise.
     */
    public boolean setString(String value) {
        if (value == null || value.isEmpty()) {
            return clear();
        }

        return ClipboardNative.setString(value);
    }

    /**
     * @return The image currently stored in the clipboard, or null if the clipboard contains a non-image value or is empty.
     */
    @Nullable
    public ClipboardImage getImage() {
        return ClipboardNative.getImage();
    }

    /**
     * Sets the clipboard to the specified image.
     *
     * @param image The image to set the clipboard to.
     * @return True if the operation was successful, false otherwise.
     */
    public boolean setImage(ClipboardImage image) {
        if (image == null) {
            return clear();
        }

        return ClipboardNative.setImage(image);
    }

    /**
     * Clears the clipboard.
     *
     * @return True if the operation was successful, false otherwise.
     */
    public boolean clear() {
        return ClipboardNative.clear();
    }

    /**
     * @return The current global instance of the default clipboard.
     */
    public static Clipboard getInstance() {
        if (instance == null) {
            instance = new Clipboard();
        }

        return instance;
    }

    /**
     * Loads the native library from the specified path.
     *
     * @param path The path to the native library.
     */
    public static void loadNative(Path path) {
        System.load(path.toAbsolutePath().toString());
    }

    /**
     * Loads the native library from the specified path and marks it as loaded.
     *
     * @param path The path to the native library.
     */
    public static void loadAndMarkNative(Path path) {
        loadNative(path);
        nativeLoaded = true;
    }

    /**
     * @return Whether the native library has been loaded.
     */
    public static boolean isNativeLoaded() {
        return nativeLoaded;
    }

    /**
     * Marks the native library as loaded.
     */
    public static void markNativeLoaded() {
        nativeLoaded = true;
    }

    /**
     * @return Whether the native library is available.
     */
    public static boolean isNativeAvailable() {
        return Clipboard.class.getResource(String.format("/natives/%s", ClipboardNative.getDefaultNativeFileName())) != null;
    }

    /**
     * Attempts to load the default native library provided by the library.
     */
    private static void attemptLoadDefaultNative() throws IOException {
        boolean isDebug = Objects.equals(System.getProperty("clipboard.debug"), "true");

        String fullPath = String.format("/natives/%s", ClipboardNative.getDefaultNativeFileName());
        if (isDebug) {
            System.out.println("Native library relative path: " + fullPath);
        }

        URL url = Clipboard.class.getResource(fullPath);
        if (url == null) {
            return;
        }

        String decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
        Path path;

        if (decodedPath.startsWith("/")) {
            decodedPath = decodedPath.substring(1);
        }

        if (decodedPath.contains("!")) {
            // The resource is inside a JAR, extract it to a temporary file
            String[] parts = decodedPath.split("!");
            try (InputStream inputStream = Clipboard.class.getResourceAsStream(parts[1])) {
                if (inputStream == null) {
                    throw new IOException("Failed to extract native library from JAR: InputStream is null");
                }

                Path tempFile = Files.createTempFile("native", null);
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                path = tempFile;
            }
        } else {
            path = Paths.get(decodedPath);
        }

        if (isDebug) {
            System.out.println("Native library absolute path: " + path.toAbsolutePath());
        }

        loadAndMarkNative(path);
    }

}
