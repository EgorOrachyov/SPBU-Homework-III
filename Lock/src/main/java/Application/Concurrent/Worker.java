package Application.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Worker {

    protected IData data;

    public Worker(IData d) {
        data = d;
    }

    public void run() {

        ExecutorService service = Executors.newFixedThreadPool(4);
        for(int i = 0; i < 1000000; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    data.add(1);
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
