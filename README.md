# Copycat
Basic clipboard access in your Java applications!

---

## Usage

### Repository


<details>
    <summary>Groovy (.gradle)</summary>

```gradle
maven {
    name = "Deftu Releases"
    url = "https://maven.deftu.dev/releases"
}
```
</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```kotlin
maven(url = "https://maven.deftu.dev/releases") {
    name = "Deftu Releases"
}
```
</details>

### Dependency

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/copycat?color=C33F3F&name=Copycat)

<details>
    <summary>Groovy (.gradle)</summary>

```gradle
implementation "dev.deftu:copycat:<VERSION>"
[
    "windows-x64",
    "windows-x86",
    "linux-x64",
    "linux-x86",
    "linux-arm",
    "linux-arm64"
].forEach { target ->
    runtimeOnly "dev.deftu:copycat-natives-$target:<VERSION>"
}
```

</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```gradle
implementation("dev.deftu:copycat:<VERSION>")
listOf(
    "windows-x64",
    "windows-x86",
    "linux-x64",
    "linux-x86",
    "linux-arm",
    "linux-arm64"
).forEach { target ->
    runtimeOnly("dev.deftu:copycat-natives-$target:<VERSION>")
}
```

</details>

### Operating system & architecture support

- Windows x64
- Windows x86
- Linux x64
- Linux x86
- Linux ARM (Android)
- Linux ARM64 (Android)

macOS is not supported at this time due to the inability to build the native library.

### Interacting with the clipboard

A user-facing API is provided via `dev.deftu.clipboard.Clipboard`, pointing to the native methods provided by the native library by default.

```java
import dev.deftu.clipboard.Clipboard;

public class Main {
    public static void main(String[] args) {
        Clipboard clipboard = Clipboard.getInstance();
        clipboard.setString("Hello, world!");
        System.out.println(clipboard.getString());
    }
}
```

### Native loading

By default, when a `Clipboard` instance is created, it will make an attempt to load the default native library provided by Copycat. If you'd rather load the natives later, or provide your own, you can do so by marking the native as loaded using `Clipboard#markNativeLoaded()`.

```java
import dev.deftu.clipboard.Clipboard;

public class Main {
    public static void main(String[] args) {
        Clipboard.markNativeLoaded();
        Clipboard clipboard = Clipboard.getInstance();
        clipboard.setString("Hello, world!"); // Throws an UnsatisfiedLinkError! There are no natives loaded.
        System.out.println(clipboard.getString());
    }
}
```

### Custom natives

If you'd like to provide your own natives, you can do so by using the `Clipboard#loadAndMarkNative(String)` method. Alternatively, you can separately use the `Clipboard#loadNative(String)` method to load the native without marking it as loaded and then use `Clipboard#markNativeLoaded()` to mark it as loaded.

```java
import dev.deftu.clipboard.Clipboard;

public class Main {
    public static void main(String[] args) {
        Clipboard.loadAndMarkNative("path/to/native");
        Clipboard clipboard = Clipboard.getInstance();
        clipboard.setString("Hello, world!");
        System.out.println(clipboard.getString());
    }
}
```

OR

```java
import dev.deftu.clipboard.Clipboard;

public class Main {
    public static void main(String[] args) {
        Clipboard.loadNative("path/to/native");
        Clipboard.markNativeLoaded();
        Clipboard clipboard = Clipboard.getInstance();
        clipboard.setString("Hello, world!");
        System.out.println(clipboard.getString());
    }
}
```

### Working with images

Copycat provides a way to interact with images on the clipboard. This is done through the `dev.deftu.clipboard.ClipboardImage` class.

```java
import dev.deftu.clipboard.Clipboard;
import dev.deftu.clipboard.ClipboardImage;

public class Main {
    public static void main(String[] args) {
        Clipboard clipboard = Clipboard.getInstance();
        ClipboardImage image = clipboard.getImage();
        System.out.println(image.getWidth() + "x" + image.getHeight());
    }
}
```

To keep the library agnostic of image libraries, the image is represented simply as a byte array, width and height.

### AWT BufferedImage support

If you're using AWT's `BufferedImage`, Copycat provides an extension library to convert between `BufferedImage` and `ClipboardImage`.

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/copycat-image-awt?color=C33F3F&name=Copycat+Image+AWT)

<details>
    <summary>Groovy (.gradle)</summary>

```gradle
implementation "dev.deftu:copycat-image-awt:<VERSION>"
```

</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```gradle
implementation("dev.deftu:copycat-image-awt:<VERSION>")
```

</details>

```java
import dev.deftu.clipboard.Clipboard;
import dev.deftu.clipboard.ClipboardImage;
import dev.deftu.clipboard.BufferedClipboardImage;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        Clipboard clipboard = Clipboard.getInstance();
        ClipboardImage image = clipboard.getImage();
        
        BufferedImage bufferedImage = BufferedClipboardImage.toBufferedImage(image);
        ClipboardImage clipboardImage = BufferedClipboardImage.toClipboardImage(bufferedImage);
        
        System.out.println(bufferedImage.getWidth() + "x" + bufferedImage.getHeight());
        
        try {
            ImageIO.write(bufferedImage, "png", new File("image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---


**This project is licensed under [LGPL-3.0][lgpl]**\
**&copy; 2024 Deftu**

[lgpl]: https://www.gnu.org/licenses/lgpl-3.0.en.html
