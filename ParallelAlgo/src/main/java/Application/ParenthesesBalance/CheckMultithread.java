package Application.ParenthesesBalance;

import Application.TaskHandler;
import Application.Util;

public class CheckMultithread {

    private final Thread thread[];
    private final TaskHandler handler[];

    /**
     * @param threadCounts Should be power of 2. If it is not, then threads count will be chosen
     *                     as the nearest power of 2 to the argument
     */
    public CheckMultithread(int threadCounts) {
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
     * Check balance for the string built from parentheses
     * @param a String
     * @return True if it is balanced
     */
    public boolean compute(String a) {

        if (a.length() <= thread.length + 1) {
            return Check.compute(a);
        }

        TaskHandler localHandler = new TaskHandler();

        final int length = a.length();
        final int threadsCount = thread.length;
        final int blocks = threadsCount + 1;
        final int start = 0;
        final int first = 0;
        final int index = 0;

        byte[] in = a.getBytes();
        int[]  outBuffer1 = new int[threadsCount + 1];
        int[]  outBuffer2 = new int[threadsCount + 1];

        recreatePool();
        localHandler.setTask(new ParallelScan(in, outBuffer1, outBuffer2,
                start, blocks, length, index, first, threadsCount, handler, thread));
        localHandler.run();

        return (outBuffer1[index] == 0 && outBuffer2[index] == 0);
    }

    public static void main(String ... args) {

        String a = "(()()())((((()()))()))";
        String b = ")(()()())((((()()))()))";
        String c = "()()()()()()())()()(()";
        String d = "()()()(((())(())(((((((((())))))))))(()()())((((()()))()))";

        CheckMultithread checkmt = new CheckMultithread(8);

        System.out.println(Check.compute(a));
        System.out.println(checkmt.compute(a));

        System.out.println(Check.compute(b));
        System.out.println(checkmt.compute(b));

        System.out.println(Check.compute(c));
        System.out.println(checkmt.compute(c));

        System.out.println(Check.compute(d));
        System.out.println(checkmt.compute(d));
    }

}
