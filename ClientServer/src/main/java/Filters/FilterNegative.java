package Filters;

import Filter.Color;
import Filter.FilterBehavior;
import Filter.Image;

public class FilterNegative implements FilterBehavior {

    private Image source;
    private int[] data;

    @Override
    public int processPixel(int x, int y) {
        Color color = Color.fromABGR(data[y * source.getWidth() + x]);
        color.multiplyToThis(-1);

        return (new Color(255, 255, 255)).add(color).getABGRColor();
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
        return "Negative";
    }

    @Override
    public String getDescription() {
        return "Transforms image in negative color";
    }

}
