package Application.TTAS;

import Application.Concurrent.*;

import java.util.concurrent.locks.Lock;

public class Test {

    public static void main(String ... args) {

        Lock lock = new TTASLock();
        Data data = new Data();
        Worker worker = new LockWorker(data, 500000, lock);

        worker.run();
        worker.showResult();

    }
}
