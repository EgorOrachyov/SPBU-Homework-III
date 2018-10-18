package Application.LongAdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Load {

    public static DecimalValue fromFile(String fileName) {

        DecimalValue res = null;

        try (Scanner in = new Scanner(new File(fileName))) {

            res = new DecimalValue(in.next());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return res;

    }

}
