package Application.Concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedList<Element> extends IList<Element> {

    private Node<Element> head;
    private Node<Element> tail;
    private Lock lock;
    private int elementsCount;

    public LinkedList() {
        head = null;
        tail = null;
        elementsCount = 0;
        lock = new ReentrantLock();
    }

    @Override
    public void add(Element element) {
        lock.lock();

        if (head == null) {
            head = tail = new Node<>(element);
            lock.unlock();
        }
        else {
            lock.unlock();

            tail.lock();
            Node<Element> newTail = new Node<>(element);
            newTail.lock();
            tail.setNext(newTail);
            tail.unlock();
            tail = newTail;
            newTail.unlock();
        }

        elementsCount += 1;
    }

    @Override
    public Element get(int index) {
        if (index >= elementsCount) {
            return null;
        }

        int i = 0;
        head.lock();
        Node<Element> current = head;
        Node<Element> next;
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
        Node<Element> current = head;
        Node<Element> next;
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

    public Node<Element> getHead() {
        return head;
    }

    public Node<Element> getTail() {
        return tail;
    }

    public int getElementsCount() {
        return elementsCount;
    }

    private class Node<T> {

        private Node<T> next;
        private T data;
        private Lock lock;

        Node(T data) {
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

        void setNext(Node<T> next) {
            this.next = next;
        }

        Node<T> getNext() {
            return next;
        }

        T getData() {
            return data;
        }
    }

}


