package Application.TurtleMovement;

import Application.Util;
import Application.TaskHandler;

public class TransformMultithread {

    private final Thread thread[];
    private final TaskHandler handler[];

    /**
     * @param threadCounts Should be power of 2. If it is not, then threads count will be chosen
     *                     as the nearest power of 2 to the argument
     */
    public TransformMultithread(int threadCounts) {
        int THREAD_COUNTS = Util.toPowerOfTwo(threadCounts);

        if (THREAD_COUNTS <= 1) {
            THREAD_COUNTS = 3;
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
     * Compute final movement from point (0,0) for object via transformations pairs
     * (a,d), where:
     *          a - counterclockwise rotation angle
     *          b - directional offset
     * @param movements (a,b) pairs
     * @return position in the 2d space
     */
    public Vector2d move(Vector2d[] movements) {

        if (thread.length + 1 >= movements.length) {
            return Transform.move(movements);
        }

        TaskHandler localHandler = new TaskHandler();

        final int length = movements.length;
        final int threadsCount = thread.length;
        final int blocks = threadsCount + 1;
        final int start = 0;
        final int first = 0;
        final int index = 0;

        Vector2d[] in = movements;
        Vector2d[] pos = new Vector2d[blocks];
        double[] angle = new double[blocks];

        recreatePool();
        localHandler.setTask(new ParallelScan(in, pos, angle,
                start, blocks, length, index, first, threadsCount, handler, thread));
        localHandler.run();

        return pos[index];
    }

    public static void main(String ... args) {

        Vector2d[] a = new Vector2d[]{
                new Vector2d(0.0, 1.0),
                new Vector2d(90.0, 1.0),
                new Vector2d(135.0, 2.0),
                new Vector2d(10.0, 7.0),
                new Vector2d(55.0, 2.0),
                new Vector2d(45.0, 0.9),
                new Vector2d(100.0, 2.0),
                new Vector2d(-30.0, 1.0),
                new Vector2d(-60.0, 1.0),
                new Vector2d(0.0, 1.0),
                new Vector2d(-80.0, 1.0),
                new Vector2d(10.0, 0.7),
                new Vector2d(-50.0, 2.0),
                new Vector2d(180.0, 3.0),
                new Vector2d(30.0, 1.0)
        };

        Vector2d[] b = new Vector2d[]{
                new Vector2d(0.0, 1.0),
                new Vector2d(90.0, 1.0),
                new Vector2d(135.0, 2.0),
                new Vector2d(10.0, 7.0),
                new Vector2d(55.0, 2.0),
                new Vector2d(45.0, 0.9),
                new Vector2d(58.0, -3.0),
                new Vector2d(45.0, 0.9),
                new Vector2d(100.0, 2.0),
                new Vector2d(-30.0, 1.0),
                new Vector2d(-60.0, 1.0),
                new Vector2d(0.0, 1.0),
                new Vector2d(-35.0, 13.0),
                new Vector2d(-80.0, 1.0),
                new Vector2d(10.0, 0.7),
                new Vector2d(-50.0, 2.0),
                new Vector2d(180.0, 3.0),
                new Vector2d(30.0, 1.0)
        };

        Vector2d[] c = new Vector2d[]{
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1),
                new Vector2d(45.0, 1)
        };

        TransformMultithread transformmt = new TransformMultithread(4);

        System.out.println(Transform.move(a));
        System.out.println(transformmt.move(a));

        System.out.println(Transform.move(b));
        System.out.println(transformmt.move(b));

        System.out.println(Transform.move(c));
        System.out.println(transformmt.move(c));
    }

}
