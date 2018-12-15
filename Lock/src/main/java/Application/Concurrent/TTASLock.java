package Application.Concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TTASLock implements Lock {

    private AtomicBoolean locked;

    public TTASLock() {
        locked = new AtomicBoolean(false);
    }

    @Override
    public void lock() {
        do {
            while (locked.get()) {
                // spin or warm the stone
            }
        } while (!locked.compareAndSet(false,true));
    }

    @Override
    public void unlock() {
        locked.set(false);
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() {

    }
}
