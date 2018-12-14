package Application.Concurrent;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedList<Element> extends IList<Element> {

    private Node head;
    private Node tail;
    private Node iterator;
    private Lock lock;
    private volatile int elementsCount;

    public LinkedList() {
        head = null;
        tail = null;
        elementsCount = 0;
        lock = new ReentrantLock();
    }

    @Override
    public void add(Element element) {
        try {
            lock.lock();
            elementsCount += 1;
            if (head == null) {
                head = tail = new Node(element);
                return;
            }
        }
        finally {
            lock.unlock();
        }

        Node oldTail = tail;

        try {
            tail.lock();
            oldTail = tail;
            Node newTail = new Node(element);
            tail.setNext(newTail);
            tail = newTail;
        }
        finally {
            oldTail.unlock();
        }

    }

    @Override
    public Element get(int index) {
        if (index >= elementsCount) {
            return null;
        }

        int i = 0;
        head.lock();
        Node current = head;
        Node next;
        while (i < index) {
            current.getNext().lock();
            next = current.getNext();
            current.unlock();
            current = next;

            i += 1;
        }

        Element data = current.getData();
        current.unlock();
        return data;
    }

    @Override
    public boolean contains(Element element) {
        if (head == null) {
            return false;
        }

        head.lock();
        Node current = head;
        Node next;
        while (current.getNext() != null) {

            if (current.getData().equals(element)) {
                current.unlock();
                return true;
            }

            current.getNext().lock();
            next = current.getNext();
            current.unlock();
            current = next;
        }

        if (current.getData().equals(element)) {
            current.unlock();
            return true;
        }

        current.unlock();
        return false;
    }

    @Override
    public Element find(IFinder<Element> finder) {
        if (head == null) {
            return null;
        }

        head.lock();
        Node current = head;
        Node next;
        while (current.getNext() != null) {

            if (finder.found(current.getData())) {
                Element data = current.getData();
                current.unlock();
                return data;
            }

            current.getNext().lock();
            next = current.getNext();
            current.unlock();
            current = next;
        }

        if (finder.found(current.getData())) {
            Element data = current.getData();
            current.unlock();
            return data;
        }

        current.unlock();
        return null;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int getElementsCount() {
        return elementsCount;
    }

    private class Node {

        private Node next;
        private Element data;
        private Lock lock;

        Node(Element data) {
            this.next = null;
            this.data = data;
            this.lock = new ReentrantLock();
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }

        void setNext(Node next) {
            this.next = next;
        }

        Node getNext() {
            return next;
        }

        Element getData() {
            return data;
        }
    }

}


