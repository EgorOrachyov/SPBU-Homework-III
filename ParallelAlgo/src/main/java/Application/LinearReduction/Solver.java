package Application.LinearReduction;


public class Solver {

    /**
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
    public static int compute(int a[], int b[]) {
        if (a.length != b.length) {
            System.err.println("Solver: arrays should have equal num of indices");
            return 0;
        }

        final int length = a.length;
        if (length == 0) {
            System.err.println("Solver: empty arrays of indices");
            return 0;
        }

        int x = b[0];
        for (int i = 1; i < length; ++i) {
            x = a[i] * x + b[i];
        }

        return x;
    }

    public static void main(String ... args) {

        System.out.println(Solver.compute(new int[]{0,2,3,4}, new int[]{2,4,1,25}));

    }

}
