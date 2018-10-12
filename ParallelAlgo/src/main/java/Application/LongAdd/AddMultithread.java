package Application.LongAdd;

import Application.TaskHandler;

public class AddMultithread implements BinaryOperator {

    public static final byte C = 2;
    public static final byte M = 1;
    public static final byte N = 0;

    private final Thread thread[];
    private final TaskHandler handler[];

    public AddMultithread(int threadCounts) {
        int THREAD_COUNTS;

        if (threadCounts <= 1) {
            THREAD_COUNTS = 3;
        }
        else {
            THREAD_COUNTS = threadCounts - 1;
        }

        thread = new Thread[THREAD_COUNTS];
        handler = new TaskHandler[THREAD_COUNTS];
        for(int i = 0; i < THREAD_COUNTS; ++i) {
            handler[i] = new TaskHandler();
            thread[i] = new Thread(handler[i]);
        }
    }

    @Override
    public DecimalValue apply(DecimalValue a, DecimalValue b) {

        // Use basic sequential add and nothing more if numbers' length less than threads count
        if (Math.max(a.getBuffer().length, b.getBuffer().length) <= thread.length) {
            return (new Add()).apply(a, b);
        }


        // Use alignment for buffers size which depends on !Total! threads count (threadsCount + 1)
        final int threadsCount = thread.length;
        final int length = align(Math.max(a.getBuffer().length, b.getBuffer().length), threadsCount + 1);
        final int step = length / (threadsCount + 1);
        TaskHandler localHandler = new TaskHandler();

        byte[] v1 = new byte[length + 1];
        byte[] v2 = new byte[length + 1];
        byte[] res = new byte[length + 1];
        byte[] carry = new byte[length + 1];

        System.arraycopy(a.getBuffer(), 0, v1, 0, a.getBuffer().length);
        System.arraycopy(b.getBuffer(), 0, v2, 0, b.getBuffer().length);

        for(int i = 0; i < threadsCount; ++i) {
            handler[i].setTask(new ComputePrediction(v1, v2, res, step * (i + 1), step * (i + 2) - 1));
            thread[i].start();
        }

        localHandler.setTask(new ComputePrediction(v1, v2, res, 0, step - 1));
        localHandler.run();

        join();

        System.out.println(new DecimalValue(res, length));




        return null;
    }

    private int align(int value, int alignment) {
        if (value % alignment == 0) {
            return value;
        }
        else {
            return value + (alignment - (value % alignment));
        }
    }

    private void join() {
        try {
            for(Thread t : thread) {
                t.join();
            }
        } catch (InterruptedException e) {

        }
    }

    public static void main(String ... args) {

        DecimalValue f = new DecimalValue("123485449");
        DecimalValue g = new DecimalValue("243253455");

        AddMultithread addMultithread = new AddMultithread(8);

        addMultithread.apply(f, g);

    }
}
