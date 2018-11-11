package Application.Unsafe;

import Application.Concurrent.IData;
import Application.Concurrent.Worker;

public class Test {

    public static void main(String ... args) {

        Data data = new Data();
        Worker worker = new Worker(data);

        worker.run();
        worker.showResult();

    }

}

class Data implements IData{

    private int i;

    Data() {
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