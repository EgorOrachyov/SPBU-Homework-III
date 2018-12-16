package Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Encapsulates common methods for working with BufferedImage
 * Provides serialization methods
 * Loading / saving images on HDD
 */
public class Image {

    private BufferedImage data;

    public Image(int width, int height) {
        data = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public Image(int width, int height, int[] bytes) {
        this(width, height);
        data.setRGB(0, 0, width, height, bytes, 0, width);
    }

    public Image(int width, int height, String buffer) {
        this(width, height);

        byte[] raw = buffer.getBytes();
        int[] bytes = new int[width * height];

        int k = 0;
        for (int i = 0; i < width * height * 4; i +=4 ) {
            int v = raw[i    ] << Color.BIT_OFFSET_24 |
                    raw[i + 1] << Color.BIT_OFFSET_16 |
                    raw[i + 2] << Color.BIT_OFFSET_8  |
                    raw[i + 3] ;
            bytes[k] = v;
            k += 1;
        }

        data.setRGB(0, 0, width, height, bytes, 0, width);
    }

    public Image(String filename) throws IOException {
        data = ImageIO.read(new File(filename));
    }

    public void saveImage(String filename, String format) throws IOException {
        ImageIO.write(data, format, new File(filename));
    }

    public void saveImage(String filename) throws IOException {
        saveImage(filename, "png");
    }

    public BufferedImage getData() {
        return data;
    }

    public void setBytes(int[] bytes) {
        data.setRGB(0, 0, getWidth(), getHeight(), bytes, 0, getWidth());
    }

    public int[] serialize() {
        return data.getRGB(
                0,
                0,
                data.getWidth(),
                data.getHeight(),
                null,
                0,
                data.getWidth()
        );
    }

    public int getWidth() {
        return data.getWidth();
    }

    public int getHeight() {
        return data.getHeight();
    }

}
