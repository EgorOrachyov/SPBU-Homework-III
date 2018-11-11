package Application.Unsafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String ... args) {

        Data data = new Data();

        ExecutorService service = Executors.newFixedThreadPool(4);
        for(int i = 0; i < 1000000; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    data.i += 1;
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

        System.out.println(data.i);
    }

}

class Data {

    public int i;

    Data() {
        i = 0;
    }

}