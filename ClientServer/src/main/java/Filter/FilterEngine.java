package Filter;

/**
 * FilterEngine engine for processing images via special
 * ProcessClass behavior, which defines how to blur (pass)
 * image
 */
public class FilterEngine {

    /**
     * Applies filter to source image and saves it in result image
     *
     * @param source Image to filter
     * @param result Image to store result - should be the same size as
     *               source image
     */
    public static void apply(Image source, Image result, FilterBehavior behavior) {
        int data[] = result.serialize();

        int width = source.getWidth();
        int height = source.getHeight();

        behavior.prepareProcess(source);

        for(int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                data[y * width + x] = behavior.processPixel(x, y);
            }
        }

        behavior.finishProcess();
        result.setBytes(data);
    }

    /**
     * Applies filter to source image, creates new images, saves result and
     * returns it
     *
     * @param source Image to filter
     * @return Result filtered image the same size as source image
     */
    public static Image apply(Image source, FilterBehavior behavior) {
        Image result = new Image(source.getWidth(), source.getHeight());
        apply(source, result, behavior);
        return result;
    }

}
