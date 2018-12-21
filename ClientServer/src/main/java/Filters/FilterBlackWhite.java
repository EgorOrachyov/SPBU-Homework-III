package Filters;

import Filter.Color;
import Filter.FilterBehavior;
import Filter.Image;

public class FilterBlackWhite implements FilterBehavior {

    private Image source;
    private int[] data;

    @Override
    public int processPixel(int x, int y) {
        Color color = Color.fromABGR(data[y * source.getWidth() + x]);

        final float length = color.lengthRGB();
        final float maxLength = 442.0f;
        final int c = (int)(length / maxLength * 255.0f);

        return (new Color(c, c, c)).getABGRColor();
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
        return "Black White";
    }

    @Override
    public String getDescription() {
        return "Transforms image in black-white colors only";
    }

}
