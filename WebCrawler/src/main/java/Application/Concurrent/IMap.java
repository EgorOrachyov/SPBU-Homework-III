package Application.Concurrent;

public abstract class IMap<Key, Value> {

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or
     *         null if there was no mapping for key.
     */
    public abstract Value add(Key key, Value value);

    /**
     * Returns the value associated with that key of null
     * if there is nothing associated with that key
     *
     * @param key key with which the value is associated
     * @return
     */
    public abstract Value get(Key key);

    /**
     * Checks whether something is associated with the key and
     * returns true or otherwise false
     *
     * @param key key to check the association with
     * @return true ih there is something associated in the map
     */
    public abstract boolean contains(Key key);

}
