package Application.LongAdd;

import Application.Task;
import Application.Util;

public class ParallelPrefixSumDistribute implements Task {

    private byte[] c;
    private int index;
    private int delta;
    private int step;

    /**
     * @warnig To work properly deltaLeft should be power of 2
     *
     * @param inout Array to save result of ppx
     * @param blockIndex Index of the block to unit the result of ppx of block[blockIndex - deltaLeft] and block[blockIndex]
     *                   blockIndex should be enumerated from 3
     * @param deltaLeft Value to get left block from current to merge it
     * @param step Number of elements in one block
     */
    public ParallelPrefixSumDistribute(byte[] inout, int blockIndex, int deltaLeft, int step) {
        c = inout;
        index = blockIndex;
        delta = deltaLeft;
        this.step = step;
    }

    @Override
    public void run() {
        if (canUnite(index - delta, index)) {
            final int prev = (index - delta) * step - 1;
            final int start = (index - 1) * step;
            final int last = (index) * step - 1;
            c[start] = AddMultithread.operator(c[prev],c[start]);
            for(int i = start + 1; i <= last; ++i) {
                c[i] = AddMultithread.operator(c[i - 1], c[i]);
            }
        }
        else {
            (new ParallelPrefixSumDistribute(c, index - delta, delta(index - delta), step)).run();

            final int prev = (index - delta) * step - 1;
            final int start = (index - 1) * step;
            final int last = (index) * step - 1;
            c[start] = AddMultithread.operator(c[prev],c[start]);
            for(int i = start + 1; i <= last; ++i) {
                c[i] = AddMultithread.operator(c[i - 1], c[i]);
            }
        }
    }

    private static boolean canUnite(int left, int right) {
        return Util.isPowerOfTwo(left) && Util.isPowerOfTwo(right - left);
    }

    private static int delta(int left) {
        int i = 0;
        int a = left;
        while ((a & 1) == 0) {
            a = a >> 1;
            i += 1;
        }

        return (1 << i);
    }
}
