package Application.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PetersonLockWorker extends Worker {

    protected ILock lock;

    public PetersonLockWorker(IData d, int size, ILock lock) {
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
            service.execute(new Task(i));
        }

        try {
            service.shutdown();
            service.awaitTermination(25, TimeUnit.SECONDS);
            service.shutdownNow();
        } catch (InterruptedException e) {
            // ignore exceptions
        }

    }

    private class Task implements Runnable {

        private int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for(int i = 0; i < size; i++) {
                try {
                    lock.lock(id);
                    data.add(1);
                }
                finally {
                    lock.unlock(id);
                }
            }
        }
    }

}
