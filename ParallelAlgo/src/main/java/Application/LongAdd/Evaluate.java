package Application.LongAdd;

import Application.Task;

public class Evaluate implements Task {

    private byte[] a;
    private byte[] b;
    private byte[] carry;
    private byte[] out;
    private int start;
    private int last;

    public Evaluate(byte[] a, byte[] b,
                    byte[] carry, byte[] out,
                    int start, int last) {
        this.a = a;
        this.b = b;
        this.carry = carry;
        this.out = out;
        this.start = start;
        this.last = last;
    }

    @Override
    public void run() {
        for(int i = start; i <= last; ++i) {
            out[i] = (byte)((a[i] + b[i] + AddMultithread.willCarry(carry[i - 1])) % 10);
        }
    }
}
