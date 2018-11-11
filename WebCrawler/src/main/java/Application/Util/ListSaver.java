package Application.Util;

import Application.Concurrent.LinkedList;

/**
 * Threads unsafe page saver
 */
public class ListSaver implements ISaver {

    private LinkedList<String> data;

    public ListSaver() {
        data = new LinkedList<>();
    }

    @Override
    public boolean save(String document, String name) {
        data.add(document);
        return true;
    }

    public LinkedList<String> getData() {
        return data;
    }
}
