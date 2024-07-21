import dev.deftu.clipboard.Clipboard;
import dev.deftu.clipboard.ClipboardImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ClipboardTest {

    static {
        System.setProperty("clipboard.debug", "true");
    }

    private static final String TEST_STRING = "Hello from Java!";

    @Test
    public void clipboardStringTests() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied string (or null)
        String clipboardString = clipboard.getString();
        System.out.println("(before set) Clipboard string: " + clipboardString);

        // Set the clipboard contents to our test string
        clipboard.setString(TEST_STRING);

        // Update our stored clipboard string (should always = TEST_STRING)
        clipboardString = clipboard.getString();
        System.out.println("(after set) Clipboard string: " + clipboardString);

        Assertions.assertEquals(TEST_STRING, clipboardString);
    }

    @Test
    public void clipboardNullStringTests() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied string (or null)
        String clipboardString = clipboard.getString();
        System.out.println("(before set) Clipboard string: " + clipboardString);

        // Set the clipboard contents to null (clears the clipboard)
        clipboard.setString(null);

        // Update our stored clipboard string (should always = null)
        clipboardString = clipboard.getString();
        System.out.println("(after set) Clipboard string: " + clipboardString);

        Assertions.assertNull(clipboardString);
    }

    @Test
    public void clipboardImageTests() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied image (or null)
        ClipboardImage currentImage = clipboard.getImage();
        System.out.println("(before set) Clipboard image: " + (currentImage == null ? "null" : currentImage.toString(false)));

        // Create a test image
        ClipboardImage testImage = createColorPickerImage();

        // Set the clipboard contents to our test image
        clipboard.setImage(testImage);

        // Update our stored clipboard image (should always = testImage)
        currentImage = clipboard.getImage();
        System.out.println("(after set) Clipboard image: " + currentImage.toString(false));

        Assertions.assertEquals(testImage, currentImage);
    }

    @Test
    public void clipboardNullImageTests() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied image (or null)
        ClipboardImage currentImage = clipboard.getImage();
        System.out.println("(before set) Clipboard image: " + (currentImage == null ? "null" : currentImage.toString(false)));

        // Set the clipboard contents to null (clears the clipboard)
        clipboard.setImage(null);

        // Update our stored clipboard image (should always = null)
        currentImage = clipboard.getImage();
        System.out.println("(after set) Clipboard image: " + (currentImage == null ? "null" : currentImage.toString(false)));

        Assertions.assertNull(currentImage);
    }

    @Test
    public void clipboardStringThenImageTest() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied string (or null)
        String clipboardString = clipboard.getString();
        System.out.println("(before set) Clipboard string: " + clipboardString);

        // Set the clipboard contents to our test string
        clipboard.setString(TEST_STRING);

        // Get the currently copied image (should always = null)
        ClipboardImage currentImage = clipboard.getImage();
        System.out.println("(after set) Clipboard image: " + (currentImage == null ? "null" : currentImage.toString(false)));

        Assertions.assertNull(currentImage);
    }

    @Test
    public void clipboardImageThenStringTest() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Get the currently copied image (or null)
        ClipboardImage currentImage = clipboard.getImage();
        System.out.println("(before set) Clipboard image: " + (currentImage == null ? "null" : currentImage.toString(false)));

        // Create a test image
        ClipboardImage testImage = createColorPickerImage();

        // Set the clipboard contents to our test image
        clipboard.setImage(testImage);

        // Get the currently copied string (should always = null)
        String clipboardString = clipboard.getString();
        System.out.println("(after set) Clipboard string: " + clipboardString);

        Assertions.assertNull(clipboardString);
    }

    @Test
    public void clearTest() {
        // Get a clipboard (also loads native)
        Clipboard clipboard = Clipboard.getInstance();

        // Clear the clipboard
        clipboard.clear();

        // Get the currently copied string (or null)
        String clipboardString = clipboard.getString();
        System.out.println("Clipboard string: " + clipboardString);
        Assertions.assertNull(clipboardString);

        // Get the currently copied image (or null)
        ClipboardImage clipboardImage = clipboard.getImage();
        System.out.println("Clipboard image: " + (clipboardImage == null ? "null" : clipboardImage.toString(false)));
        Assertions.assertNull(clipboardImage);
    }

    private ClipboardImage createColorPickerImage() {
        int width = 512;
        int height = 512;
        byte[] data = new byte[width * height * 4];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = (y * width + x) * 4;

                float hue = (float) x / (width - 1);
                float saturation = (float) y / (height - 1);
                float brightness = 1.0f - ((float) x / (width - 1) * (float) y / (height - 1));

                // Convert HSV to RGB
                int rgb = Color.HSBtoRGB(hue, saturation, brightness);

                data[index] = (byte) ((rgb >> 16) & 0xFF);
                data[index + 1] = (byte) ((rgb >> 8) & 0xFF);
                data[index + 2] = (byte) (rgb & 0xFF);
                data[index + 3] = (byte) 255;
            }
        }

        return new ClipboardImage(width, height, data);
    }

}
