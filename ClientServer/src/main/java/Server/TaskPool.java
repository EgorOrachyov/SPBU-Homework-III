package Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskPool {

    private final int threadsCount;
    private ExecutorService executorService;

    public TaskPool(int threadsCount) {
        this.threadsCount = threadsCount;
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    public void submitTask(TaskHandler task) {
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }

}
