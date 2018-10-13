package Application.LinearReduction;

import Application.Task;
import Application.TaskHandler;

public class ParallelScan implements Task {

    private int[] a;
    private int[] b;
    private int[] c1;
    private int[] c2;
    private int start;
    private int blocks;
    private int count;
    private int index;
    private int first;
    private int threadsCount;
    private TaskHandler[] handler;
    private Thread[] thread;

    /**
     * Parallel scam implements binary operator defined for tuple in
     * the following way:
     *
     * (a,b) x (c,d) = (a * c,b * c + d)
     *
     * This operator is associative (it can be proven by simple test)
     *
     * @param a Array of a indices
     * @param b Array of b indices
     * @param out1 First element in tuple to save
     * @param out2 Second element in touple to save
     * @param start First index of range to start
     * @param blocks Count of blocks to handle
     * @param count Num of element to handle
     * @param index Index in out1 and out2 arrays to save result
     * @param first First thread to use in the threads array
     * @param threadsCount Num of available threads
     * @param handlers TaskHandlers
     * @param threads Threads
     */
    public ParallelScan(int[] a, int[] b, int[] out1, int[] out2,
                        int start, int blocks, int count, int index,
                        int first, int threadsCount,
                        TaskHandler[] handlers,
                        Thread[] threads) {

        this.a = a;
        this.b = b;
        c1 = out1;
        c2 = out2;
        this.start = start;
        this.blocks = blocks;
        this.count = count;
        this.index = index;
        this.first = first;
        this.threadsCount = threadsCount;
        handler = handlers;
        thread = threads;
    }

    @Override
    public void run() {
        if (blocks == 1) {
            int t1 = a[start];
            int t2 = b[start];
            for(int i = start + 1; i < start + count; ++i) {
                t1 = t1 * a[i];
                t2 = t2 * a[i] + b[i];
            }

            c1[index] = t1;
            c2[index] = t2;
        }
        else {
            final int newBlocks = blocks / 2;
            final int countLeft = count / 2;
            final int countRight = count - countLeft;
            final int starttLeft = start;
            final int startRight = start + countLeft;
            final int indexLeft = index;
            final int indexRight = index + newBlocks;
            final int threadsLeft = threadsCount / 2;
            final int threadsRight = threadsCount / 2;
            final int firstLeft = first + 1;
            final int firstRight = first + threadsLeft + 1;

            handler[first].setTask(new ParallelScan(a, b, c1, c2,
                    startRight, newBlocks, countRight, indexRight,
                    firstRight, threadsRight,
                    handler, thread));

            thread[first].start();

            (new ParallelScan(a, b, c1, c2, starttLeft, newBlocks, countLeft, indexLeft, firstLeft, threadsLeft, handler, thread)).run();

            try {
                thread[first].join();
            } catch (InterruptedException e) {}

            c1[index] = c1[indexLeft] * c1[indexRight];
            c2[index] = c2[indexLeft] * c1[indexRight] + c2[indexRight];
        }
    }
}
