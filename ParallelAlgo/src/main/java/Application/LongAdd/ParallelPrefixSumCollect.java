package Application.LongAdd;

import Application.Task;
import Application.TaskHandler;

public class ParallelPrefixSumCollect implements Task {

    private byte[] a;
    private byte[] c;
    private int start;
    private int step;
    private int blocks;
    private int first;
    private int count;
    private TaskHandler[] handler;
    private Thread[] thread;

    /**
     * @warnig To work properly num of blocks should be power of 2
     * @warnig To work properly count of threads  should be power of 2 minus one
     *
     * @param in Array of predictions (CMN)
     * @param out Array to save result of pps
     * @param start Index of the first element in the arrays in and out of this handled block
     * @param step Step between blocks
     * @param blocks Number of blocks in this section
     * @param first Index of the first thread and handler
     * @param count Count of threads and handlers
     * @param handlers Task Handler
     * @param threads Threads to run task
     */
    public ParallelPrefixSumCollect(byte[] in, byte[] out,
                                    int start, int step, int blocks,
                                    int first, int count,
                                    TaskHandler[] handlers,
                                    Thread[] threads) {
        a = in;
        c = out;
        this.start = start;
        this.step = step;
        this.blocks = blocks;
        this.first = first;
        this.count = count;
        handler = handlers;
        thread = threads;
    }

    @Override
    public void run() {

        if (count == 0) {
            c[start] = AddMultithread.operator(AddMultithread.M, a[start]);
            for(int i = start + 1; i < start + step; ++i) {
                c[i] = AddMultithread.operator(c[i - 1], a[i]);
            }
        }
        else {
            final int newBlocks = blocks / 2;
            final int newCount = count / 2;

            handler[first + newCount].setTask(new ParallelPrefixSumCollect(a, c, start, step, newBlocks ,first, newCount, handler, thread));
            thread[first + newCount].start();

            if (newCount == 0) {
                (new ParallelPrefixSumCollect(a, c, start + step * newBlocks, step, 1, first, 0,null, null)).run();
            } else {
                handler[first + count - 1].setTask(new ParallelPrefixSumCollect(a, c, start + step * newBlocks, step, newBlocks, first + newCount + 1, newCount, handler, thread));
                handler[first + count - 1].run();
            }

            try {
                thread[first + newCount].join();
            } catch (InterruptedException e) {}

            c[start + (blocks - 1) * step] = AddMultithread.operator(c[start + newBlocks * step - 1], c[start + (blocks - 1) * step]);
            for(int i = start + (blocks - 1) * step ; i < start + blocks * step; ++i) {
                c[i] = AddMultithread.operator(c[i - 1], c[i]);
            }
        }
    }
}
