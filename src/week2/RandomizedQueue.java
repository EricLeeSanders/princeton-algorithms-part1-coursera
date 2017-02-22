package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Queue that removes random items.
 * 
 * @author Eric
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;

    @SuppressWarnings("unchecked")
    private Item[] itemArray = (Item[]) new Object[16];

    /**
     * Determines if the queue is empty.
     */
    public boolean isEmpty() {

        return size == 0;
    }

    /**
     * Returns the size of the queue.
     */
    public int size() {

        return size;
    }

    /**
     * Adds an item to the queue.
     * 
     * @param item
     */
    public void enqueue(Item item) {

        if (item == null) {
            throw new NullPointerException();
        }

        if (size >= itemArray.length) {
            resizeArray(itemArray.length * 2);
        }

        itemArray[size++] = item;
    }

    /**
     * Removes and returns a random item from the queue.
     * 
     * @return
     */
    public Item dequeue() {

        if (size <= 0) {
            throw new NoSuchElementException();
        }

        int randInt = StdRandom.uniform(size);
        Item item = itemArray[randInt];

        swapRemovedItemWithEnd(randInt);

        size--;

        if (size > 0 && size <= itemArray.length / 4) {
            resizeArray(itemArray.length / 2);
        }

        return item;
    }

    /**
     * Returns but does not remove a random item from the queue.
     */
    public Item sample() {

        if (size <= 0) {
            throw new NoSuchElementException();
        }

        int randInt = StdRandom.uniform(size);
        Item item = itemArray[randInt];

        return item;
    }

    /**
     * Returns an independent iterator over the items in the queue in random
     * order.
     */
    public Iterator<Item> iterator() {

        @SuppressWarnings("unchecked")
        Iterator<Item> iter = new Iterator<Item>() {

            Item[] iterArray;
            int index = 0;
            {
                iterArray = (Item[]) new Object[size];
                for (int i = 0; i < size; i++) {
                    iterArray[i] = itemArray[i];
                }
                StdRandom.shuffle(iterArray);
            }

            @Override
            public boolean hasNext() {

                return index < (iterArray.length);
            }

            @Override
            public Item next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iterArray[index++];
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };

        return iter;
    }

    /**
     * Moves an item to the end of the array.
     * 
     * @param removedIndex
     */
    private void swapRemovedItemWithEnd(int removedIndex) {

        itemArray[removedIndex] = itemArray[size - 1];
        itemArray[size - 1] = null;
    }

    /**
     * Resizes the array.
     * 
     * @param newSize
     */
    @SuppressWarnings("unchecked")
    private void resizeArray(int newSize) {

        Item[] resizedItemArray = (Item[]) new Object[newSize];

        for (int i = 0; i < size; i++) {
            resizedItemArray[i] = itemArray[i];
        }

        itemArray = resizedItemArray;

    }

}
