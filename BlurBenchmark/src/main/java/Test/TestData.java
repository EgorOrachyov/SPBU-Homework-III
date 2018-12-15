package Test;

import Filter.Blur.AverageBlur;
import Filter.Common.Image;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Encapsulate picture source for test, range type as
 * blur param and threads count
 */
public class TestData {

    public Image picture;
    public ArrayList<Integer> threads;
    public int rangeType;

    TestData(String pictureName, int rangeType) {
        this.rangeType = rangeType;

        try {
            picture = new Image(pictureName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        threads = new ArrayList<>(5);
        for (int i = 0; i < 5; ++i) {
            threads.add(0x1 << i);
        }
    }
}
