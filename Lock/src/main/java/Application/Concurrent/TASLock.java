package Application.Concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock implements ILock {

    private AtomicBoolean locked;

    public TASLock() {
        locked = new AtomicBoolean(false);
    }

    @Override
    public void lock(int threadID) {
        while (locked.getAndSet(true)) {
            // spin or warm the stone
        }
    }

    @Override
    public void unlock(int threadID) {
        locked.set(false);
    }

}
