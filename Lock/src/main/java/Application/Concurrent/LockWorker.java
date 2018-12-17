package Application.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class LockWorker extends Worker {

    protected Lock lock;

    public LockWorker(IData d, int size, Lock lock) {
        super(d, size);
        this.lock = lock;
    }

    @Override
    public void run() {
        run(2);
    }

    public void run(int threadsCount) {

        size /= threadsCount;
        ExecutorService service = Executors.newFixedThreadPool(threadsCount);
        for(int i = 0; i < threadsCount; i++) {
            service.execute(new Task());
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
        @Override
        public void run() {
            for(int i = 0; i < size; i++) {
                try {
                    lock.lock();
                    data.add(1);
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }

}
