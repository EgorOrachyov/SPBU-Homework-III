package Application.TurtleMovement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Load {

    public static Vector2d[] fromFile(String fileName) {

        Vector2d[] d = null;

        try (Scanner in = new Scanner(new File(fileName))) {
            ArrayList<Vector2d> tmp = new ArrayList<>(64);

            while (in.hasNext()) {
                tmp.add(new Vector2d(in.nextDouble(), in.nextDouble()));
            }

            d = (Vector2d[]) tmp.toArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return d;
    }

}
