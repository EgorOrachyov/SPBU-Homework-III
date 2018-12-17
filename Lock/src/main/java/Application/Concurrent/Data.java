package Application.Concurrent;

public class Data implements IData {

    private int i;

    public Data() {
        i = 0;
    }

    @Override
    public void add(int value) {
        i += value;
    }

    @Override
    public int get() {
        return i;
    }

}
