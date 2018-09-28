package Filter.Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    public Image(String filename) {
        try {
            data = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String filename, String format) {
        try {
            ImageIO.write(data, format, new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String filename) {
        saveImage(filename, "png");
    }

    public BufferedImage getData() {
        return data;
    }

}
