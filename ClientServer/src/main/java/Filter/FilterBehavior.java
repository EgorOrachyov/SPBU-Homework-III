package Filter;

/**
 * Outside defined specific
 */
public interface FilterBehavior {

    /**
     * Process one pixel of source image
     * @param x X pixel's coordinate
     * @param y Y pixel's coordinate
     * @return Processed new pixel's color
     */
    int processPixel(int x, int y);

    /**
     * Setup filter source and results images to get
     * access to stored data
     * @param source Image to filter
     * @param result Image to save result
     * @return True if setup is succeed
     */
    boolean prepareProcess(Image source, Image result);

    /**
     * Finishes image processing
     * @return True if process is finished correctly
     */
    boolean finishProcess();

    /**
     * @return Defined filter name
     */
    String getFilterName();

    /**
     * @return Some brief description of the filter
     */
    String getDescription();
}
