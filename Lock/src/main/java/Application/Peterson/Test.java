package Application.Peterson;

import Application.Concurrent.IData;
import Application.Concurrent.ILock;
import Application.Concurrent.PetersonLock;
import Application.Concurrent.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String ... args) {

        Data data = new Data();
        Worker worker = new PetersonWorker(data);

        worker.run();
        worker.showResult();

    }

}

class PetersonWorker extends Worker {

    private ILock lock;

    public PetersonWorker(Data d) {
        super(d);
        lock = new PetersonLock();
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
