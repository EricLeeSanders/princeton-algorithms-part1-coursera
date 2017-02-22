package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Deque class that allows Stack operations. Can insert and remove from bottom
 * and top. Uses a Linked List for it's implementation.
 * 
 * @author Eric
 *
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * Represents a Node in the Stack
     * 
     * @author Eric
     *
     */
    private class Node {
        
        Item value;
        Node previous, next;

        public Node(Item value, Node previous, Node next) {
            
            this.value = value;
            this.previous = previous;
            this.next = next;
        }

        public Node() {

        }
    }

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    /**
     * Constructs an empty deque
     */
    public Deque() {
        
        head = new Node();
        tail = new Node();

        head.next = tail;
        tail.previous = head;

    }

    /**
     * Determines if the deque is empty
     */
    public boolean isEmpty() {

        return size == 0;
    }

    /**
     * Returns the size of the Deque
     */
    public int size() {

        return size;
    }

    /**
     * Adds an item to the Deque
     * 
     * @param item
     */
    public void addFirst(Item item) {

        if (item == null) {
            throw new NullPointerException();
        }

        Node newNode = new Node(item, head, head.next);
        head.next.previous = newNode;
        head.next = newNode;

        size++;

    }

    /**
     * Adds an item to the end of the Deque.
     * 
     * @param item
     */
    public void addLast(Item item) {

        if (item == null) {
            throw new NullPointerException();
        }

        Node newNode = new Node(item, tail.previous, tail);
        tail.previous.next = newNode;
        tail.previous = newNode;

        size++;

    }

    /**
     * Removes and returns the first item from the Deque.
     */
    public Item removeFirst() {

        if (size <= 0) {
            throw new NoSuchElementException();
        }

        Node oldNode = head.next;
        oldNode.next.previous = head;
        head.next = oldNode.next;

        size--;

        return oldNode.value;
    }

    /**
     * Removes and returns the last item from the Deque.
     */
    public Item removeLast() {

        if (size <= 0) {
            throw new NoSuchElementException();
        }

        Node oldNode = tail.previous;
        oldNode.previous.next = tail;
        tail.previous = oldNode.previous;

        size--;

        return oldNode.value;
    }

    /**
     * Returns an iterator that iterates over the items from front to end.
     */
    @Override
    public Iterator<Item> iterator() {

        Iterator<Item> iter = new Iterator<Item>() {

            private Node currentNode = head;

            @Override
            public boolean hasNext() {

                return currentNode.next != null && currentNode.next != tail;
            }

            @Override
            public Item next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                currentNode = currentNode.next;

                return currentNode.value;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }

        };

        return iter;
    }
}
