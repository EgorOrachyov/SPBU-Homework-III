package Client;

import Filter.Image;

public class FilterTask {

    private final Image source;
    private final int filterId;

    public FilterTask(Image source, int filterId) {
        this.source = source;
        this.filterId = filterId;
    }

    public Image getSource() {
        return source;
    }

    public int getFilterId() {
        return filterId;
    }

}
