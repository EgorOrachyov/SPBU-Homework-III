package Filter.Common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Encapsulates common methods for
 * working with hard driver images
 */
public class Image {

    private BufferedImage data;

    public Image(int width, int height) {
            data = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
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

}
