package Application.LinearReduction;

import Application.TaskHandler;
import Application.Util;

public class SolverMultithread {

    private final Thread thread[];
    private final TaskHandler handler[];

    /**
     * @param threadCounts Should be power of 2. If it is not, then threads count will be chosen
     *                     as the nearest power of 2 to the argument
     */
    public SolverMultithread(int threadCounts) {
        int THREAD_COUNTS = Util.toPowerOfTwo(threadCounts);

        if (THREAD_COUNTS <= 1) {
            THREAD_COUNTS = 2;
        }
        else {
            THREAD_COUNTS = THREAD_COUNTS - 1;
        }

        thread = new Thread[THREAD_COUNTS];
        handler = new TaskHandler[THREAD_COUNTS];
        for(int i = 0; i < THREAD_COUNTS; ++i) {
            handler[i] = new TaskHandler();
        }
    }

    private void recreatePool() {
        for(int i = 0; i < thread.length; ++i) {
            thread[i] = new Thread(handler[i]);
        }
    }

    /**
     * /**
     * Reduces a sequence of linear equations as follows:
     * x0 = b[0]
     * x1 = a[1] * x0 + b[1]
     * x2 = a[2] * x1 + b[2]
     * ...
     * xN = a[N] * xN-1 + b[N] ,
     * where N = a.length - 1
     *
     * @warning Arrays a and b should have equal lengths
     *
     * @param a Indices a
     * @param b Indices b
     * @return xN
     */
    public int compute(int[] a, int[] b) {
        if (a.length != b.length) {
            System.err.println("SolverMultithread: arrays should have equal num of indices");
            return 0;
        }

        final int length = a.length;
        if (length == 0) {
            System.err.println("SolverMultithread: empty arrays of indices");
            return 0;
        }

        if (thread.length + 1 >= length) {
            return Solver.compute(a,b);
        }

        TaskHandler localHandler = new TaskHandler();
        final int threadsCount = thread.length;
        final int blocks = threadsCount + 1;
        final int start = 0;
        final int firts = 0;
        final int index = 0;
        int[] outBuffer1 = new int[threadsCount + 1];
        int[] outBuffer2 = new int[threadsCount + 1];

        // Magic swap to handle start of our linear reduction
        a[0] = b[0];
        b[0] = 0;

        recreatePool();
        localHandler.setTask(new ParallelScan(a, b, outBuffer1, outBuffer2,
                start, blocks, length, index, firts, threadsCount, handler, thread));
        localHandler.run();

        return outBuffer1[index] + outBuffer2[index];
    }

    public static void main(String ... args) {

        final int[] a =  new int[]{2,4,1,25,12,3,4,5,6,-3,-44,1,1};
        final int[] b =  new int[]{0,2,3,4, 5, 6,7,8,9,1, 22, 3,0};

        final int[] c =  new int[]{2,4,1,25,12,3,4,5,6,-3,-44,1,1,2, -1,5,6,7,8,1,1,1,1,1};
        final int[] d =  new int[]{0,2,3,4, 5, 6,7,8,9,1, 22, 3,0,12,-8,0,0,1,2,3,1,2,1,3};

        final int[] e =  new int[]{2,4,1,25,12,3,4,5,6,-3,-44,1,1, 2,-1,5,6,7,8,1,1,4,1, 0,-7, -2,9,8,11,16,13,1};
        final int[] f =  new int[]{0,2,3, 4, 5,6,7,8,9, 1, 22,3,0,12,-8,0,0,1,2,3,1,2,1,12,44,222,1,0, 0, 0, 1,1};

        SolverMultithread solvermt = new SolverMultithread(8);

        System.out.println(Solver.compute(a, b));
        System.out.println(solvermt.compute(a, b));

        System.out.println(Solver.compute(b, a));
        System.out.println(solvermt.compute(b, a));

        System.out.println(Solver.compute(c, d));
        System.out.println(solvermt.compute(c, d));

        System.out.println(Solver.compute(e, f));
        System.out.println(solvermt.compute(e, f));
    }

}
