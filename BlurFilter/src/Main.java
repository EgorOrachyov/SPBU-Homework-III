import Filter.Blur.AverageBlur;
import Filter.Common.Image;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        AverageBlur filter = new AverageBlur(AverageBlur.RANGE_TYPE_11);
        filter.setThreadsCount(8);
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);

        try {
            Image source = new Image("pictures/source.png");
            Image result = filter.apply(source);
            result.saveImage("pictures/result.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
