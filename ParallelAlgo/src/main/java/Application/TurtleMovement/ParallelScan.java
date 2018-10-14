package Application.TurtleMovement;

import Application.Task;
import Application.TaskHandler;

public class ParallelScan implements Task {

    private Vector2d[] in;
    private Vector2d[] pos;
    private double[] angles;
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
     * ((x,y),a) x ((z,w),b) = ((x + x',y + y'), a + b)
     *
     * where
     *
     * | x'| = | cos a   -sin a | * | x |
     * | y'|   | sin a    cos a |   | y |
     *
     * This operator is associative (it can be proven by simple test)
     *
     * @param in Array of pairs (angle,offset)
     * @param pos Array of positions (x,y)
     * @param angles Array of angles tmp
     * @param start First index of range to start
     * @param blocks Count of blocks to handle
     * @param count Num of element to handle
     * @param index Index in out1 and out2 arrays to save result
     * @param first First thread to use in the threads array
     * @param threadsCount Num of available threads
     * @param handlers TaskHandlers
     * @param threads Threads
     */
    public ParallelScan(Vector2d[] in, Vector2d[] pos, double[] angles,
                        int start, int blocks, int count, int index,
                        int first, int threadsCount,
                        TaskHandler[] handlers,
                        Thread[] threads) {

        this.in = in;
        this.pos = pos;
        this.angles = angles;
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
            double posX = 0;
            double posY = 0;
            double angle = 0;

            for (int i = start; i < start + count; ++i) {
                double rot = Math.toRadians(in[i].x());
                double offset = in[i].y();

                angle += rot;
                posX += offset * Math.cos(angle);
                posY += offset * Math.sin(angle);
            }

            angles[index] = angle;
            pos[index] = new Vector2d(posX, posY);
        }
        else {
            final int newBlocks = blocks / 2;
            final int countLeft = count / 2;
            final int countRight = count - countLeft;
            final int startLeft = start;
            final int startRight = start + countLeft;
            final int indexLeft = index;
            final int indexRight = index + newBlocks;
            final int threadsLeft = threadsCount / 2;
            final int threadsRight = threadsCount / 2;
            final int firstLeft = first + 1;
            final int firstRight = first + threadsLeft + 1;

            handler[first].setTask(new ParallelScan(in, pos, angles,
                    startRight, newBlocks, countRight, indexRight,
                    firstRight, threadsRight,
                    handler, thread));

            thread[first].start();

            (new ParallelScan(in, pos, angles, startLeft, newBlocks, countLeft, indexLeft, firstLeft, threadsLeft, handler, thread)).run();

            try {
                thread[first].join();
            } catch (InterruptedException e) {}

            Matrix2x2d m = new Matrix2x2d(
                    Math.cos(angles[indexLeft]), -Math.sin(angles[indexLeft]),
                    Math.sin(angles[indexLeft]), Math.cos(angles[indexLeft])
            );

            angles[index] = angles[indexLeft] + angles[indexRight];
            pos[index] = pos[indexLeft].add(m.multiply(pos[indexRight]));
        }
    }
}
