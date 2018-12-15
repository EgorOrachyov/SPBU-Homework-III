package Filter.Blur;

import Filter.IFilter;
import Filter.Common.Color;
import Filter.Common.Image;

import java.awt.image.BufferedImage;

/**
 * Simple image blur for square sectors of pixels
 * with equal weights in canvas
 */
public class AverageBlur implements IFilter {

    /**
     * Common range types
     */
    public static final int RANGE_TYPE_5  = 5;
    public static final int RANGE_TYPE_7  = 7;
    public static final int RANGE_TYPE_9  = 9;
    public static final int RANGE_TYPE_11 = 11;
    public static final int RANGE_TYPE_13 = 13;
    public static final int RANGE_TYPE_15 = 15;
    public static final int RANGE_TYPE_17 = 17;
    public static final int RANGE_TYPE_19 = 19;

    private int threadsCount;
    private PassType passType;
    private int range;

    public AverageBlur() {
        range = RANGE_TYPE_7;
        threadsCount = 4;
        passType = PassType.HORIZONTAL;
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

        if (passType == PassType.HORIZONTAL) {
            int heightPart = imageHeight / threadsCount;

            for (int i = 0; i < threadsCount; ++i) {
                threads[i] = new Thread(
                        new AverageBlurPass(0, heightPart * i, imageWidth, heightPart * (i + 1), data)
                );
                threads[i].start();
            }
        }
        else if (passType == PassType.VERTICAL) {
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
        try {
            for (int i = 0; i < threadsCount; ++i) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Image apply(Image source) {
        Image result = new Image(source.getData().getWidth(), source.getData().getHeight());
        apply(source, result);
        return result;
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

    public void setRange(int range) {
        if (range > 1) {
            this.range = range / 2 + 1;
        } else {
            this.range = RANGE_TYPE_7;
        }
    }

    public void setPassType(PassType passType) {
        this.passType = passType;
    }

    public int getRange() {
        return range;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public PassType getPassType() {
        return passType;
    }

}

class AverageBlurData {

    public int width;
    public int height;
    public int range;
    public int[] srcColor;
    public int[] outColor;
    public PassType passType;
    public BufferedImage src;
    public BufferedImage out;

    public AverageBlurData(int width, int height,
                           PassType passType, int range,
                           Image source, Image result) {
        this.width = width;
        this.height= height;
        this.passType = passType;
        this.range = range;

        src = source.getData();
        out = result.getData();

        // Get 1-dimensional arrays of buffers' pixels' colors
        srcColor = src.getRGB(0,0, width, height, null, 0 ,width);
        outColor = out.getRGB(0, 0, width, height, null, 0, width);
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
        if (data.passType == PassType.HORIZONTAL) {
            for (int y = startY; y < height; ++y) {
                for (int x = startX; x < width; ++x) {
                    blurIgnoreBorders(x, y);
                }
            }
        }
        else if (data.passType == PassType.VERTICAL) {
            for (int x = startX; x < width; ++x) {
                for (int y = startY; y < height; ++y) {
                    blurIgnoreBorders(x, y);
                }
            }
        }

        // save data in out buffered image
        data.out.setRGB(0, 0, data.width, data.height, data.outColor, 0, width);
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
                    result.addToThis(colorFromABGRInt(data.srcColor[(y + k) * data.width + (x + m)]));
                }
            }
        }

        result.multiplyToThis(1.0f / samplesCount);
        data.outColor[y * data.width + x] = result.getABGRColor();
    }
}