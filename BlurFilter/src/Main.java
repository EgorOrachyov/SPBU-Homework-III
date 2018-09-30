import Filter.Blur.AverageBlur;
import Filter.Common.Image;

import java.io.IOException;
import java.util.Scanner;

/**
 * Console interface style application entry point
 * (Handles Average blur)
 */
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
            // Invite for input
            System.out.print("-> ");

            Image source;
            Image result;
            String[] parts;
            String filename;
            String outfilename;
            String tmp;
            final String exit = "exit";
            final String range = "range";
            final String threads = "threads";
            final int NUM_OF_PARAMS = 2;
            final int SKIP_FILES_NAMES = 2;
            final int TAKE_NEXT_ONE = 1;
            int rangeType = AverageBlur.RANGE_TYPE_7;
            int threadsCount = 4;
            boolean errorOccured = false;

            // Get user input, check for exit cmd
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

            // Got files' names, check for additional params
            for (int i = 0; i < NUM_OF_PARAMS; ++i) {
                try {
                    if (parts.length >= SKIP_FILES_NAMES + NUM_OF_PARAMS * (i + 1)) {
                        tmp = parts[SKIP_FILES_NAMES + NUM_OF_PARAMS * i];
                        if (tmp.equals(range)) {
                            rangeType = Integer.valueOf(parts[SKIP_FILES_NAMES + NUM_OF_PARAMS * i + TAKE_NEXT_ONE]);
                        } else if (tmp.equals(threads)) {
                            threadsCount = Integer.valueOf(parts[SKIP_FILES_NAMES + NUM_OF_PARAMS * i + TAKE_NEXT_ONE]);
                        } else {
                            errorOccured = true;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong command line params");
                    errorOccured = true;
                }
            }

            if (errorOccured) continue;

            // Get resource for blur
            try {
                source = new Image(filename);
            } catch (IOException e) {
                System.out.println("Cannot open file with name " + filename);
                continue;
            }

            // Set properties and blur
            filter.setRange(rangeType);
            filter.setThreadsCount(threadsCount);
            result = filter.apply(source);

            // Try to save result
            try {
                String extension = "png";
                result.saveImage(outfilename, extension);
            } catch (IOException e) {
                System.out.println("Cannot open out file with name " + outfilename);
            }
        }
    }
}
