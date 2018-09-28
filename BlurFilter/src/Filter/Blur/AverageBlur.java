package Filter.Blur;

import Filter.IFilter;
import Filter.Tools.Color;
import Filter.Tools.Image;

import java.awt.image.BufferedImage;

/**
 * Simple image blur for square sectors of pixels
 * with equal weights in canvas
 */
public class AverageBlur implements IFilter {

    public static final int RANGE_TYPE_5  = 2;
    public static final int RANGE_TYPE_7  = 3;
    public static final int RANGE_TYPE_9  = 4;
    public static final int RANGE_TYPE_11 = 5;
    public static final int RANGE_TYPE_13 = 6;
    public static final int RANGE_TYPE_15 = 7;
    public static final int RANGE_TYPE_17 = 8;
    public static final int RANGE_TYPE_19 = 9;

    public static final int PASS_TYPE_VERTICAL   = 1;
    public static final int PASS_TYPE_HORIZONTAL = 2;

    private int threadsCount;
    private int passType;
    private int range;

    private AverageBlur() {
        threadsCount = 4;
        passType = PASS_TYPE_HORIZONTAL;
    }

    /**
     * Blur ignore pixels out of the picture
     * @param rangeType Range type for filtered pixels
     */
    public AverageBlur(int rangeType) {
        this();
        range = rangeType;
    }

    public void apply(Image source, Image result) {

        if ((source.getData().getWidth() != result.getData().getWidth()) ||
           (source.getData().getHeight() != result.getData().getHeight())) {
            return;
        }

        int imageWidth = source.getData().getWidth();
        int imageHeight = source.getData().getHeight();

        AverageBlurData data = new AverageBlurData(
                imageWidth, imageHeight,
                passType, range, source, result);

        Thread[] threads = new Thread[threadsCount];

        if (passType == PASS_TYPE_HORIZONTAL) {
            int heightPart = imageHeight / threadsCount;

            for (int i = 0; i < threadsCount; ++i) {
                threads[i] = new Thread(
                        new AverageBlurPass(0, heightPart * i, imageWidth, heightPart * (i + 1), data)
                );
                threads[i].start();
            }
        }
        else if (passType == PASS_TYPE_VERTICAL) {
            int widthPart = imageWidth / threadsCount;

            for (int i = 0; i < threadsCount; ++i) {
                threads[i] = new Thread(
                        new AverageBlurPass(widthPart * i, 0, widthPart * (i + 1), imageHeight, data)
                );
                threads[i].start();
            }
        }
        else {
            return;
        }

        // Wait for ending of our threads' work
        boolean threadsAreWorking = true;
        while (threadsAreWorking) {
            threadsAreWorking = false;
            for (int i = 0; i < threadsCount; ++i) {
                threadsAreWorking |= threads[i].isAlive();
            }
        }

    }

    public Image apply(Image source) {
        Image result = new Image(source.getData().getWidth(), source.getData().getHeight());
        apply(source, result);
        return result;
    }

    public int getRange() {
        return range;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    /**
     * Set desired number of threads for parallelism of the task
     * @param threadsCount Number of threads to use
     */
    public void setThreadsCount(int threadsCount) {
        if (threadsCount >= 1) {
            this.threadsCount = threadsCount;
        }
    }

    public int getPassType() {
        return passType;
    }

    public void setPassType(int passType) {
        this.passType = passType;
    }
}

class AverageBlurData {

    public int width;
    public int height;
    public int passType;
    public int range;
    public BufferedImage src;
    public BufferedImage out;

    public AverageBlurData(int width, int height,
                           int passType, int range,
                           Image source, Image result) {
        this.width = width;
        this.height= height;
        this.passType = passType;
        this.range = range;

        src = source.getData();
        out = result.getData();
    }

}

class AverageBlurPass implements Runnable {

    private int startX;
    private int startY;
    private int width;
    private int height;
    private AverageBlurData data;

    AverageBlurPass(int x, int y, int width, int height, AverageBlurData data) {
        startX = x;
        startY = y;

        this.width = width;
        this.height = height;
        this.data = data;
    }

    public void run() {
        if (data.passType == AverageBlur.PASS_TYPE_HORIZONTAL) {
            for (int y = startY; y < height; ++y) {
                for (int x = startX; x < width; ++x) {
                    blurIgnoreBorders(x, y);
                }
            }
        }
        else if (data.passType == AverageBlur.PASS_TYPE_VERTICAL) {
            for (int x = startX; x < width; ++x) {
                for (int y = startY; y < height; ++y) {
                    blurIgnoreBorders(x, y);
                }
            }
        }
    }

    private boolean isPointInCanvas(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < data.width) && (y < data.height);
    }

    private Color colorFromABGRInt(int ABGR) {
        return new Color(ABGR & 0xFF, (ABGR & 0xFF00) >>> 8,
                (ABGR & 0xFF0000) >>> 16, (ABGR & 0xFF000000) >>> 24);
    }

    private void blurIgnoreBorders(int x, int y) {
        int samplesCount = 0;
        Color result = new Color(0,0,0,0);

        for (int k = -data.range; k <= data.range; ++k) {
            for (int m = -data.range; m <= data.range; ++m) {
                if (isPointInCanvas(x + m, y + k)) {
                    samplesCount += 1;
                    result.addToThis(colorFromABGRInt(data.src.getRGB(x + m, y + k)));
                }
            }
        }

        result.multiplyToThis(1.0f / samplesCount);
        data.out.setRGB(x, y, result.getABGRColor());
    }

}