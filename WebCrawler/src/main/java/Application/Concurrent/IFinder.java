package Application.Concurrent;

public abstract class IFinder<T> {

    /**
     * Checks whether the object should be found or not
     * by special terms
     * @param suspected Object to check
     * @return true If the object is something what
     *         should be found
     */
    protected abstract boolean found(T suspected);

}
