package Application.Concurrent;

public interface ILock {

    void lock(int threadID);

    void unlock(int threadID);

}
