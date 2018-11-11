package Application.Concurrent;

public class PetersonLock implements ILock {

    private boolean[] flags = {false, false};
    private int turn = 0;

    @Override
    public void lock(int threadID) {
        flags[threadID] = true;
        turn = 1 - threadID;
        while (flags[turn] == true && turn != threadID) {
            // spin <=> warm the stone
        }
    }

    @Override
    public void unlock(int threadID) {
        flags[threadID] = false;
    }

}
