package Application.LinearReduction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Load {

    public static Data fromFile(String fileName) {

        Data d = null;

        try (Scanner in = new Scanner(new File(fileName))) {
            ArrayList<Integer> inA = new ArrayList<>(64);
            ArrayList<Integer> inB = new ArrayList<>(64);

            while (in.hasNext()) {
                inA.add(in.nextInt());
                inB.add(in.nextInt());
            }

            d = new Data(convertIntegers(inA),convertIntegers(inB));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return d;
    }

    private static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }

}
