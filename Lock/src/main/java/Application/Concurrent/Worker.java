package Application.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Worker {

    protected IData data;
    protected int size;

    public Worker(IData d, int size) {
        this.data = d;
        this.size = size;
    }

    public void run() {

        ExecutorService service = Executors.newFixedThreadPool(2);
        for(int i = 0; i < 2; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < size; i++) {
                        data.add(1);
                    }
                }
            });
        }

        try {
            service.shutdown();
            service.awaitTermination(10, TimeUnit.SECONDS);
            service.shutdownNow();
        } catch (InterruptedException e) {
            // ignore exceptions
        }

    }

    public void showResult() {
        System.out.println(data.get());
    }

}
