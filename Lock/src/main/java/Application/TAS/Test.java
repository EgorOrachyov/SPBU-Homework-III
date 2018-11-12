package Application.TAS;

import Application.Concurrent.*;

public class Test {

    public static void main(String ... args) {

        ILock lock = new TASLock();
        Data data = new Data();
        Worker worker = new LockWorker(data, 500000, lock);

        worker.run();
        worker.showResult();

    }
}



