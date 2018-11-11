package Application.Concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/**
 * Fixed size thread pool with concurrent queue for
 * handling the tasks' order of completion
 */
public class ThreadPool implements Executor, AutoCloseable {

    private ConcurrentLinkedQueue<Runnable> queue;      // Queue of the tasks to do
    private Thread[] threads;                           // Working threads
    private boolean isRunning = true;                   // Shows if should stop immediately even if the queue is not empty
    private boolean isFinishing = false;                // Shows if we should finish all previous tasks

    public ThreadPool(int threadsCount) {
        if (threadsCount <= 0) {
            threadsCount = 2;
            System.err.println("ThreadPool: threads count should be more then 0");
        }

        queue = new ConcurrentLinkedQueue<>();
        threads = new Thread[threadsCount];
        for(int i = 0; i < threadsCount; i++) {
            threads[i] = new Thread(new Worker());
            threads[i].start();
        }

    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted
     */
    public void shutdown() {
        isRunning = false;
    }

    /**
     * Close the thread pool when all the submitted tasks
     * ara completed
     */
    public void finish() {
        isFinishing = true;
    }

    @Override
    public void execute(Runnable command) {
        if (isRunning && !isFinishing) {
            queue.add(command);
        }
    }

    @Override
    public void close() {
        shutdown();
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                Runnable task = queue.poll();

                if (task == null) {
                    if (isFinishing) {
                        break;
                    }
                }
                else {
                    task.run();
                }
            }
        }

    }
}
