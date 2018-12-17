package Application.LongAdd;

import Application.Task;

public class ComputePrediction implements Task {

    private byte[] a;
    private byte[] b;
    private byte[] c;
    private final int start;
    private final int last;

    public ComputePrediction(byte[] in1, byte[] in2, byte[] out, int start, int last) {
        a = in1;
        b = in2;
        c = out;

        this.start = start;
        this.last = last;
    }

    @Override
    public void run() {
        for(int i = start; i <= last; ++i) {
            final int sum = a[i] + b[i];
            if (sum > 9) {
                c[i] = AddMultithread.C;
            }
            else if (sum < 9) {
                c[i] = AddMultithread.N;
            }
            else {
                c[i] = AddMultithread.M;
            }
        }
    }
}
