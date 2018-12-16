package Filters;

import Filter.Color;
import Filter.FilterBehavior;
import Filter.Image;

public class FilterAverageBlur implements FilterBehavior {

    private static final int RANGE = 5; // half of the range 11

    private Image source;
    private int[] data;

    @Override
    public int processPixel(int x, int y) {
        int samples = 0;
        Color color = new Color();

        for (int k = -RANGE; k <= RANGE; ++k) {
            for (int m = -RANGE; m <= RANGE; ++m) {
                if (isPointInCanvas(x + m, y + k)) {
                    samples += 1;
                    color.addToThis(Color.fromABGR(data[(y + k) * source.getWidth() + (x + m)]));
                }
            }
        }

        color.multiplyToThis(1.0f / samples);
        return color.getABGRColor();
    }

    @Override
    public boolean prepareProcess(Image source) {
        this.source = source;
        this.data = source.serialize();

        return true;
    }

    @Override
    public boolean finishProcess() {
        return true;
    }

    @Override
    public String getName() {
        return "Average Blur";
    }

    @Override
    public String getDescription() {
        return "Blurs the image by averaging the color values in each pixel";
    }

    private boolean isPointInCanvas(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < source.getWidth()) & (y < source.getHeight());
    }

}
