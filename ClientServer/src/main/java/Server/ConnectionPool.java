package Server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private final long initialDelay;
    private final long period;
    private ScheduledExecutorService executor;

    /**
     * Creates new scheduled connection pool to process in cycles connection handlers
     * @param initialDelay Delay in MILLISECONDS to setup connection
     * @param period       Period in MILLISECONDS to check-out client side activity
     * @param threadsCount Count of threads for pool to handle tasks (priority = 8)
     */
    public ConnectionPool(long initialDelay, long period, int threadsCount) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.executor = Executors.newScheduledThreadPool(threadsCount);
    }

    public ScheduledFuture<?> submitConnection(ConnectionHandler handler) {
        return executor.scheduleAtFixedRate(handler, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }

}
