package Filters;

import Filter.FilterBehavior;
import Filter.Image;

public class FilterDefault implements FilterBehavior {

    private Image source;

    @Override
    public int processPixel(int x, int y) {
        return source.getData().getRGB(x, y);
    }

    @Override
    public boolean prepareProcess(Image source) {
        this.source = source;

        return true;
    }

    @Override
    public boolean finishProcess() {
        return true;
    }

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public String getDescription() {
        return "Returns default image without changes";
    }

}
