package Application.Concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent blocking hash map (hash table - in terms of task used as a hash set)
 * based upon blocking Linked List
 *
 * Does not allow delete elements
 * Does not allow resize (rebuild) table
 * Provides independent access to lists for threads:
 *
 *            ____________
 *            | List 1   | -> [thread 1]
 *            |__________|
 *            | List 2   | -> [thread 2]
 *            |__________|
 * Table  ->  | ...      |
 *            |__________|
 *            | List N   | -> [thread M]
 *            |__________|
 *
 * Bottleneck in getting list
 *
 * @param <Key>   Value, which will be hashed, to get list
 * @param <Value> Unique value stored in table (for different values keys can be equal)
 */
public class HashMap<Key,Value> extends IMap<Key,Value> {

    private int range;
    private volatile int elementsCount;
    private LinkedList<Element>[] map;
    private Lock lock;

    public HashMap() {
        this(64);
    }

    public HashMap(int range) {
        if (range <= 0) {
            System.err.println("HashMap: range should be more then 0");
            range = 16;
        }

        this.range = range;
        this.elementsCount = 0;
        this.lock = new ReentrantLock();

        this.map = new LinkedList[range];
        for(int i = 0; i < range; i++) {
            this.map[i] = new LinkedList<>();
        }
    }

    @Override
    public void add(Key key, Value value) {
        int index = Math.abs(key.hashCode()) % range;
        LinkedList<Element> list = map[index];

        try {
            lock.lock();
            Element e = list.find(new IFinder<Element>() {
                @Override
                protected boolean found(Element suspected) {
                    return (suspected.getKey().equals(key));
                }
            });

            if (e != null) {
                e.setValue(value);
            }
            else {
                list.add(new Element(key, value));
                elementsCount += 1;
            }
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Value get(Key key) {
        int index = Math.abs(key.hashCode()) % range;
        LinkedList<Element> list = map[index];

        Element e = list.find(new IFinder<Element>() {
            @Override
            protected boolean found(Element suspected) {
                return (suspected.getKey().equals(key));
            }
        });

        if (e != null) {
            return e.value;
        }

        return null;
    }

    @Override
    public boolean contains(Key key) {
        int index = Math.abs(key.hashCode()) % range;
        LinkedList<Element> list = map[index];

        Element e = list.find(new IFinder<Element>() {
            @Override
            protected boolean found(Element suspected) {
                return (suspected.getKey().equals(key));
            }
        });

        return (e != null);
    }

    public int getElementsCount() {
        return elementsCount;
    }

    public int getRange() {
        return range;
    }

    private class Element {

        private Key key;
        private Value value;

        Element(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        public Key getKey() {
            return key;
        }

        public Value setValue(Value value) {
            Value tmp = this.value;
            this.value = value;
            return tmp;
        }

        public Value getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            try {
                Element tmp = (Element)obj;
                return (key.equals(tmp.key) && value.equals(tmp.value));
            }
            catch (ClassCastException e) {
                return false;
            }
        }
    }

}
