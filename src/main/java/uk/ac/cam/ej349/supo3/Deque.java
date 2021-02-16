package uk.ac.cam.ej349.supo3;

import java.util.NoSuchElementException;

public class Deque<T> {

    private  class Node {

        private Node next;
        private Node prev;
        private final T value;

        Node (T value, Node next, Node prev) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }

        Node (T value) {
            this.next = null;
            this.prev = null;
            this.value = value;
        }

        @Override
        public String toString() {
            if (next == null) {
                return String.valueOf(value);
            }
            return value + "," + next;
        }
    }

    private Node head;
    private Node tail;

    public boolean isEmpty() {
        return head == null;
    }

    public void putRear(T item) {
        if (head == null) {
            head = new Node(item);
            tail = head;
        } else {
            Node temp = tail;
            temp.next = new Node(item, null, temp);
            tail = temp.next;
        }
    }

    public void putFront(T item) {
        if (head == null) {
            head = new Node(item);
            tail = head;
        } else {
            Node temp = head;
            temp.prev = new Node(item, temp, null);
            head = temp.prev;
        }
    }

    public T getFront() {
        if (head == null) {
            throw new NoSuchElementException();
        } else if (head == tail) {
            T value = head.value;
            head = null;
            tail = null;
            return value;
        } else {
            Node temp = head;
            head = head.next;
            head.prev = null;
            return temp.value;
        }
    }

    public T getRear() {
        if (tail == null) {
            throw new NoSuchElementException();
        } else if (tail == head) {
            T value = tail.value;
            tail = null;
            head = null;
            return value;
        } else {
            Node temp = tail;
            tail = tail.prev;
            tail.next = null;
            return temp.value;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s]", head == null ? "" : head.toString());
    }

    Deque () {
        this.head = null;
        this.tail = null;
    }

    @SafeVarargs
    public static <W> Deque<W> create(W... values) {
        Deque<W> deque = new Deque<>();
        for (W value : values) {
            deque.putRear(value);
        }
        return deque;
    }

    public static void main(String[] args) {
        Deque<Integer> testDeque = create(3,2,1,5,3);
        testDeque.putRear(1);
        testDeque.putFront(5);
        System.out.println(testDeque);
        System.out.println(testDeque.getFront());
        System.out.println(testDeque);
        testDeque.getRear();
        System.out.println(testDeque);
        testDeque.getFront();
        testDeque.getFront();
        testDeque.getFront();
        System.out.println(testDeque);
        testDeque.getRear();
        System.out.println(testDeque);
        System.out.println(testDeque.tail);
        System.out.println(testDeque.head);
    }
}
