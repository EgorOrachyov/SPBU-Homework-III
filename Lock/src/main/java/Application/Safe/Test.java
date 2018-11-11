package Application.Safe;

import Application.Concurrent.IData;
import Application.Concurrent.Worker;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main(String ... args) {

        Data data = new Data();
        Worker worker = new Worker(data);

        worker.run();
        worker.showResult();
    }

}

class Data implements IData {

    private Lock lock;
    private int i;

    Data() {
        i = 0;
        lock = new ReentrantLock();
    }

    @Override
    public void add(int value) {
        lock.lock();
        i += value;
        lock.unlock();
    }

    @Override
    public int get() {
        return i;
    }
}
