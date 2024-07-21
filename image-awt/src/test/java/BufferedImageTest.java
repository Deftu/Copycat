import dev.deftu.clipboard.BufferedClipboardImage;
import dev.deftu.clipboard.Clipboard;
import dev.deftu.clipboard.ClipboardImage;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BufferedImageTest {

    @Test
    public void bufferedClipboardImageTest() throws IOException {
        Clipboard clipboard = Clipboard.getInstance();

        BufferedImage image = ImageIO.read(testFile());
        ClipboardImage readImage = new BufferedClipboardImage(image);
        clipboard.setImage(readImage);
    }

    private File testFile() {
        return Path.of("D:\\GNfWhcCXcAEYpnW.jpg").toFile();
    }

    static {
        System.setProperty("clipboard.debug", "true");
    }

}
