package Application.ParenthesesBalance;

import Application.Task;
import Application.TaskHandler;

public class ParallelScan implements Task {

    private byte[] a;
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
     * (a,b) x (c,d) = (a + c - d,b)
     *
     * This operator is associative (it can be proven by simple test)
     *
     * @param a Array of a characters
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
    public ParallelScan(byte[] a, int[] out1, int[] out2,
                        int start, int blocks, int count, int index,
                        int first, int threadsCount,
                        TaskHandler[] handlers,
                        Thread[] threads) {

        this.a = a;
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
            int unbalanced = 0;
            int balance = 0;

            for(int i = start; i < start + count; ++i) {
                if (a[i] == ')') {
                    if (balance == 0) {
                        unbalanced += 1;
                    }
                    else {
                        balance -= 1;
                    }
                }
                else {
                    balance += 1;
                }
            }

            c1[index] = unbalanced;
            c2[index] = balance;
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

            handler[first].setTask(new ParallelScan(a, c1, c2,
                    startRight, newBlocks, countRight, indexRight,
                    firstRight, threadsRight,
                    handler, thread));

            thread[first].start();

            (new ParallelScan(a, c1, c2, starttLeft, newBlocks, countLeft, indexLeft, firstLeft, threadsLeft, handler, thread)).run();

            try {
                thread[first].join();
            } catch (InterruptedException e) {}

            c1[index] = c1[indexLeft] + c1[indexRight] - c2[indexLeft];
            c2[index] = c2[indexRight];
        }
    }
}
