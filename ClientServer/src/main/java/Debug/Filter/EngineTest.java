package Debug.Filter;

import Filter.FilterBehavior;
import Filter.FilterEngine;
import Filter.Image;
import Filters.FilterAverageBlur;
import Filters.FilterBlackWhite;

import java.io.IOException;

public class EngineTest {

    public static void main(String ... args) {

        try {
            Image source = new Image("/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test1.png");

            FilterBehavior filter1 = new FilterAverageBlur();
            FilterBehavior filter2 = new FilterBlackWhite();

            Image result1 = FilterEngine.apply(source, filter1);
            Image result2 = FilterEngine.apply(source, filter2);

            result1.saveImage("/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/result1.png");
            result2.saveImage("/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/result2.png");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
