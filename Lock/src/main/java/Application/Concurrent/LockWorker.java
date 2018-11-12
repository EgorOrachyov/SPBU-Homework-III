package Application.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LockWorker extends Worker {

    protected ILock lock;

    public LockWorker(IData d, ILock lock) {
        super(d);
        this.lock = lock;
    }

    @Override
    public void run() {

        ExecutorService service = Executors.newFixedThreadPool(2);
        for(int i = 0; i < 2; i++) {
            service.execute(new Task(i));
        }

        try {
            service.shutdown();
            service.awaitTermination(10, TimeUnit.SECONDS);
            service.shutdownNow();
        } catch (InterruptedException e) {
            // ignore exceptions
        }

    }

    private class Task implements Runnable {

        private int id;

        Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for(int i = 0; i<500000; i++) {
                lock.lock(id);
                data.add(1);
                lock.unlock(id);
            }
        }
    }

}
