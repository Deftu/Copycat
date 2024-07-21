package dev.deftu.clipboard;

import java.awt.image.BufferedImage;

public class BufferedClipboardImage extends ClipboardImage {

    public BufferedClipboardImage(BufferedImage image) {
        super(image.getWidth(), image.getHeight(), toByteArray(image));
    }

    public static BufferedImage toBufferedImage(ClipboardImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);
        int index = 0;
        for (int y = 0; y < image.height; y++) {
            for (int x = 0; x < image.width; x++) {
                int color = 0;
                color |= (image.data[index++] & 0xFF) << 16;
                color |= (image.data[index++] & 0xFF) << 8;
                color |= image.data[index++] & 0xFF;
                color |= (image.data[index++] & 0xFF) << 24;
                bufferedImage.setRGB(x, y, color);
            }
        }

        return bufferedImage;
    }

    public static ClipboardImage toClipboardImage(BufferedImage image) {
        return new ClipboardImage(image.getWidth(), image.getHeight(), toByteArray(image));
    }

    private static byte[] toByteArray(BufferedImage image) {
        byte[] bytes = new byte[image.getWidth() * image.getHeight() * 4];
        int index = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                bytes[index++] = (byte) ((color >> 16) & 0xFF);
                bytes[index++] = (byte) ((color >> 8) & 0xFF);
                bytes[index++] = (byte) (color & 0xFF);
                bytes[index++] = (byte) ((color >> 24) & 0xFF);
            }
        }

        return bytes;
    }

}
