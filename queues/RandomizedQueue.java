/* *****************************************************************************
 *  Name: Bisrat Zerihun
 *  Date: 6/16/2021
 *  Description: randomized queue implementation by moi
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int numberOfItems;
    private Item[] randomQ;
    private int capacity = 1;
    private int first;
    private int last;

    /**
     * constructs a randomized queue DS
     */
    public RandomizedQueue() {
        this.randomQ = (Item[]) new Object[this.capacity];
        this.first = 0;
        this.last = -1;
        this.numberOfItems = 0;
    }

    /**
     * checks if the queue is empty
     *
     * @return true if empty and false otherwise
     */
    public boolean isEmpty() {
        return this.numberOfItems == 0;
    }

    /**
     * returns the number of items in the queue
     *
     * @return number of items in queue
     */
    public int size() {
        return this.numberOfItems;
    }

    /**
     * adds item in the queue
     *
     * @param item item to be added in the queue
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.capacity == size()) {
            resize(2 * this.capacity);
        }
        this.last++;
        this.randomQ[this.last] = item;
        this.numberOfItems++;
    }

    /**
     * removes a random item in the queue
     *
     * @return the removed item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if ((this.capacity / 4) > size()) {
            resize(this.capacity / 2);
        }
        int index = StdRandom.uniform(this.first, this.last + 1);
        Item temp = this.randomQ[index];
        this.randomQ[index] = null;
        while (index < this.last) {
            this.randomQ[index] = this.randomQ[index + 1];
            index++;
        }
        this.last--;
        this.numberOfItems--;
        return temp;
    }

    /**
     * returns a random item from queue but not remove it
     *
     * @return the random item selected
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.randomQ[StdRandom.uniform(this.numberOfItems)];
    }

    /**
     *
     */
    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        for (int i = 0; i < this.numberOfItems; i++) {
            temp[i] = this.randomQ[i];
        }
        this.first = 0;
        this.last = this.numberOfItems - 1;
        this.capacity = cap;
        this.randomQ = temp;


    }


    /**
     * Returns an iterator over elements of type {@code Item}.
     *
     * @return an Iterator.
     */
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    /**
     * unit testing
     *
     * @param args inputs to test if necessary
     */
    public static void main(String[] args) {

    }

    private class RandomIterator implements Iterator<Item> {
        private int current = numberOfItems;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return current > 0;
        }

        /**
         * deprecated method that should only return an exception
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return randomQ[--this.current];
        }
    }
}
