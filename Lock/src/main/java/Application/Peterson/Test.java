package Application.Peterson;

import Application.Concurrent.*;

public class Test {

    public static void main(String ... args) {

        ILock lock = new PetersonLock();
        Data data = new Data();
        Worker worker = new PetersonLockWorker(data, 500000, lock);

        worker.run();
        worker.showResult();

    }

}

class Data implements IData {

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
