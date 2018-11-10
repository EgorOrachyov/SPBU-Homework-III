package Application.Util;

import java.util.ArrayList;

/**
 * Threads unsafe page saver
 */
public class ArraySaver  implements ISaver {

    private ArrayList<String> data;

    public ArraySaver() {
        data = new ArrayList<>();
    }

    @Override
    public boolean save(String document, String name) {
        return data.add(document);
    }

    public ArrayList<String> getData() {
        return data;
    }
}
