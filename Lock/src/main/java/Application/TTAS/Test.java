package Application.TTAS;

import Application.Concurrent.*;

public class Test {

    public static void main(String ... args) {

        ILock lock = new TTASLock();
        Data data = new Data();
        Worker worker = new LockWorker(data, lock);

        worker.run();
        worker.showResult();

    }

}
