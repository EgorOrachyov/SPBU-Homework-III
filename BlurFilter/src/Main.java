import Filter.Blur.AverageBlur;
import Filter.Common.Image;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        AverageBlur filter = new AverageBlur();

        System.out.println("*------------------- Average Blur Filter -------------------*");
        System.out.println("* Enter file name with path to the picture to blur it       *");
        System.out.println("* with desired out file name and  properties: range =       *");
        System.out.println("* {5,7,9,11,13,15,17,19} and threads count = {1,2,4,8}.     *");
        System.out.println("* Enter only a picture name if You want to blur with        *");
        System.out.println("* default properties (range = 7, threads = 4). Enter        *");
        System.out.println("* exit to leave the application                             *");
        System.out.println("* Examples:                                                 *");
        System.out.println("* $ picture/source.png picture/result.png                   *");
        System.out.println("* $ picture/source.png picture/result.png range 9 threads 4 *");
        System.out.println("*-----------------------------------------------------------*");

        while (true) {
            System.out.print("-> ");

            Image source;
            Image result;
            String[] parts;
            String filename;
            String outfilename;
            String tmp;
            String exit = "exit";
            String range = "range";
            String threads = "threads";
            int NUM_OF_PARAMS = 2;
            int rangeType = AverageBlur.RANGE_TYPE_7;
            int threadsCount = 4;
            boolean errorOccured = false;

            tmp = in.nextLine();
            if (tmp.equals(exit)) {
                break;
            }
            else {
                parts = tmp.split(" ");
                if (parts.length < 2) {
                    continue;
                }
                else {
                    filename = parts[0];
                    outfilename = parts[1];
                }
            }

            for (int i = 0; i < NUM_OF_PARAMS; ++i) {
                if (parts.length >= 2 + 2 * (i + 1)) {
                    tmp = parts[2 + 2 * i];
                    if (tmp.equals(range)) {
                        rangeType = Integer.valueOf(parts[3 + 2 * i]);
                    } else if (tmp.equals(threads)) {
                        threadsCount = Integer.valueOf(parts[3 + 2 * i]);
                    } else {
                        errorOccured = true;
                        break;
                    }
                }
            }

            if (errorOccured) continue;

            try {
                source = new Image(filename);
            } catch (IOException e) {
                System.out.println("Cannot open file with name " + filename);
                continue;
            }

            filter.setRange(rangeType);
            filter.setThreadsCount(threadsCount);

            result = filter.apply(source);

            try {
                String extension = "png";
                result.saveImage(outfilename, extension);
            } catch (IOException e) {
                System.out.println("Cannot open out file with name " + outfilename);
            }
        }
    }
}
