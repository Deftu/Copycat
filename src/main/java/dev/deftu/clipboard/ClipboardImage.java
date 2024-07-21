package dev.deftu.clipboard;

import java.nio.ByteBuffer;

/**
 * Represents an image in the clipboard.
 */
public class ClipboardImage implements Comparable<ClipboardImage> {

    /**
     * The width of the image.
     */
    public final int width;

    /**
     * The height of the image.
     */
    public final int height;

    /**
     * The data of the image.
     */
    public final byte[] data;

    /**
     * Constructs a new {@link ClipboardImage} with the specified width, height and data.
     *
     * @param width The width of the image.
     * @param height The height of the image.
     * @param buffer The data of the image.
     */
    public ClipboardImage(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        this.data = new byte[buffer.remaining()];
        buffer.get(this.data);
    }

    /**
     * Constructs a new {@link ClipboardImage} with the specified width, height and data.
     *
     * @param width The width of the image.
     * @param height The height of the image.
     * @param data The data of the image.
     */
    public ClipboardImage(int width, int height, byte[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    @Override
    public int compareTo(ClipboardImage other) {
        if (width != other.width) {
            return Integer.compare(width, other.width);
        }

        if (height != other.height) {
            return Integer.compare(height, other.height);
        }

        for (int i = 0; i < data.length; i++) {
            if (data[i] != other.data[i]) {
                return Byte.compare(data[i], other.data[i]);
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClipboardImage) {
            return compareTo((ClipboardImage) obj) == 0;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + width;
        result = 31 * result + height;
        for (byte b : data) {
            result = 31 * result + b;
        }

        return result;
    }

    /**
     * Returns a string representation of this {@link ClipboardImage}.
     *
     * @param withBytes Whether to include the image data in the string representation.
     * @return A string representation of this {@link ClipboardImage}.
     */
    public String toString(boolean withBytes) {
        StringBuilder builder = new StringBuilder();

        builder.append(ClipboardImage.class.getSimpleName()).append("[");
        builder.append("width=").append(width).append(", ");
        builder.append("height=").append(height).append(", ");
        builder.append("data=");

        if (withBytes) {
            builder.append("[");
            for (int i = 0; i < data.length; i++) {
                builder.append(data[i]);
                if (i < data.length - 1) {
                    builder.append(", ");
                }
            }

            builder.append("]");
        } else if (data != null) {
            builder.append("[").append(data.length).append(" bytes]");
        } else {
            builder.append("null");
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(true);
    }

}
