package Application.LongAdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Load {

    public static DecimalValue fromFile(String fileName) {

        DecimalValue res = null;

        try (Scanner in = new Scanner(new File(fileName))) {

            StringBuilder builder = new StringBuilder(1024);

            while (in.hasNext()) {
                builder.append(in.next());
            }

            res = new DecimalValue(builder.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return res;

    }

}
