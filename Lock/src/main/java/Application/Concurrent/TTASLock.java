package Application.Concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock implements ILock {

    private AtomicBoolean locked;

    public TTASLock() {
        locked = new AtomicBoolean(false);
    }

    @Override
    public void lock(int threadID) {
        do {
            while (locked.get()) {
                // spin or warm the stone
            }
        } while (locked.getAndSet(true));
    }

    @Override
    public void unlock(int threadID) {
        locked.set(false);
    }
}
