package Application.LongAdd;

import Application.Task;
import Application.TaskHandler;

public class ParallelPrefixSum implements Task {

    private byte[] a;
    private byte[] c;
    private int start;
    private int step;
    private int blocks;
    private int first;
    private int count;
    private TaskHandler[] handler;
    private Thread[] thread;

    public ParallelPrefixSum(byte[] in1, byte[] out,
                             int start, int step, int blocks,
                             int first, int count,
                             TaskHandler[] handlers,
                             Thread[] threads) {
        a = in1;
        c = out;
        this.start = start;
        this.step = step;
        this.blocks = blocks;
        this.first = first;
        this.count = count;
        handler = handlers;
        thread = threads;

        System.out.printf("start: %d last: %d f thread %d l thread %d count: %d \n", start, start + step * blocks - 1, first, first + count - 1, count);
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

            handler[first + newCount].setTask(new ParallelPrefixSum(a, c, start, step, newBlocks ,first, newCount, handler, thread));
            thread[first + newCount].start();

            if (newCount == 0) {
                (new ParallelPrefixSum(a, c, start + step * newBlocks, step, 1, first, 0,null, null)).run();
            } else {
                handler[first + count - 1].setTask(new ParallelPrefixSum(a, c, start + step * newBlocks, step, newBlocks, first + newCount + 1, newCount, handler, thread));
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
