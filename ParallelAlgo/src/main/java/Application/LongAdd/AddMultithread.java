package Application.LongAdd;

import Application.TaskHandler;
import Application.Util;

/**
 * Multi-thread add algorithm via parallel prefix scan.
 * Involves 4 independent threads passes:
 * 1) Prediction pass (compute cmn)
 * 2) Parallel prefix sum (collect pass)
 * 3) Parallel prefix sum (distribute pass)
 * 4) Evaluate pass
 */
public class AddMultithread implements BinaryOperator {

    public static final byte C = 2;
    public static final byte M = 1;
    public static final byte N = 0;

    private final Thread thread[];
    private final TaskHandler handler[];

    /**
     * @param threadCounts Should be power of 2. If it is not, then threads count will be chosen
     *                     as the nearest power of 2 to the argument
     */
    public AddMultithread(int threadCounts) {
        int THREAD_COUNTS = Util.toPowerOfTwo(threadCounts);

        if (THREAD_COUNTS <= 1) {
            THREAD_COUNTS = 1;
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

    /**
     * @note if lengths of values are less then number of threads then standard
     *       single thread add will be applied
     *
     * @param a Long decimal value
     * @param b Lomg decimal value
     * @return Add a and b
     */
    @Override
    public DecimalValue apply(DecimalValue a, DecimalValue b) {

        // Use basic sequential add and nothing more if numbers' length less than threads count
        if (Math.max(a.getBuffer().length, b.getBuffer().length) <= thread.length) {
            return (new Add()).apply(a, b);
        }


        // Use alignment for buffers size which depends on !Total! threads count (threadsCount + 1)
        final int threadsCount = thread.length;
        final int length = Util.align(Math.max(a.getBuffer().length, b.getBuffer().length), threadsCount + 1);
        final int step = length / (threadsCount + 1);
        TaskHandler localHandler = new TaskHandler();

        byte[] v1 = new byte[length + 1];
        byte[] v2 = new byte[length + 1];
        byte[] res = new byte[length + 1];
        byte[] carry = new byte[length + 1];

        System.arraycopy(a.getBuffer(), 0, v1, 0, a.getBuffer().length);
        System.arraycopy(b.getBuffer(), 0, v2, 0, b.getBuffer().length);

        recreatePool();

        for(int i = 0; i < threadsCount; ++i) {
            handler[i].setTask(new ComputePrediction(v1, v2, res, step * (i + 1), step * (i + 2) - 1));
            thread[i].start();
        }

        localHandler.setTask(new ComputePrediction(v1, v2, res, 0, step - 1));
        localHandler.run();

        join();
        recreatePool();

        localHandler.setTask(new ParallelPrefixSumCollect(res, carry, 0, step, threadsCount + 1, 0, threadsCount, handler, thread));
        localHandler.run();

        if (threadsCount >= 3) {
            recreatePool();
            int currentHandler = 0;
            for (int i = 3; i < threadsCount + 1; i += 2) {
                handler[currentHandler].setTask(new ParallelPrefixSumDistribute(carry, i, 1, step));
                thread[currentHandler].start();
                currentHandler += 1;
            }

            join();
        }

        recreatePool();

        for(int i = 0; i < threadsCount; ++i) {
            handler[i].setTask(new Evaluate(v1, v2, carry, res, (i + 1) * step, (i == threadsCount - 1 ? length : (i + 2) * step - 1)));
            thread[i].start();
        }

        res[0] = (byte)((v1[0] + v2[0]) % 10);
        for(int i = 1; i <= step - 1; ++i) {
            res[i] = (byte)((v1[i] + v2[i] + AddMultithread.willCarry(carry[i - 1])) % 10);
        }

        join();

        return new DecimalValue(res);
    }

    public static byte operator(byte a, byte b) {
        if (b == AddMultithread.C || b == AddMultithread.N) {
            return b;
        }
        else {
            return a;
        }
    }

    public static byte willCarry(byte prediction) {
        return (byte)(prediction == C ? 1 : 0);
    }


    private void recreatePool() {
        for(int i = 0; i < thread.length; ++i) {
            thread[i] = new Thread(handler[i]);
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

        AddTest();

    }

    private static void AddTest() {

        DecimalValue a = new DecimalValue("3297198758148957984317985793481759898798541739");
        DecimalValue b = new DecimalValue("239823894");
        DecimalValue c = new DecimalValue("99999999999999999999999");
        DecimalValue d = new DecimalValue("1");
        DecimalValue e = new DecimalValue("0");
        DecimalValue f = new DecimalValue("1234854");
        DecimalValue g = new DecimalValue("243253455");

        DecimalValue h = new DecimalValue(
                "231764962795743987594788284386" +
                "486325462354682375974598743890" +
                "327846873420080785291194691264" +
                "919479187419847983789332667326" +
                "723979111414442412898712898999");
        DecimalValue k = new DecimalValue(
                "124765672135467521948017614857" +
                "143059038096587983795278682876" +
                "723678278587465738703480984905" +
                "809239849823148164786174678114" +
                "719874893700818939290898398297");

        Add add = new Add();

        System.out.println("Sequential add");
        System.out.println(add.apply(a,b));
        System.out.println(add.apply(a,c));
        System.out.println(add.apply(b,a));
        System.out.println(add.apply(c,d));
        System.out.println(add.apply(a,b));
        System.out.println(add.apply(e,e));
        System.out.println(add.apply(f,g));
        System.out.println(add.apply(g,b));
        System.out.println(add.apply(h,k));

        AddMultithread addmt = new AddMultithread(32);

        //System.out.println("Multi-thread add");
        //System.out.println(addmt.apply(a,b));
        //System.out.println(addmt.apply(a,c));
        //System.out.println(addmt.apply(b,a));
        //System.out.println(addmt.apply(c,d));
        //System.out.println(addmt.apply(a,b));
        //System.out.println(addmt.apply(e,e));
        //System.out.println(addmt.apply(f,g));
        System.out.println(addmt.apply(h,k));

    }
}
