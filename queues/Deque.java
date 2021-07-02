/* *****************************************************************************
 *  Name: Bisrat Zerihun
 *  Date: 06/16/2021
 *  Description: deque implementation by moi
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int numberOfItems;
    private Item[] deq;
    private int first;
    private int last;
    private int capacity = 1;

    /**
     * initalizes an empty deque DS
     */
    public Deque() {
        this.deq = (Item[]) new Object[this.capacity];
        this.numberOfItems = 0;
        this.first = 0;
        this.last = 0;
    }

    /**
     * checks if the deque is empty or not
     *
     * @return true if empty and false otherwise
     */
    public boolean isEmpty() {
        return this.numberOfItems == 0 && this.first == this.last;
    }

    /**
     * returns the number of items in the deque
     *
     * @return number of items in deque
     */
    public int size() {
        return this.numberOfItems;
    }

    /**
     * adds an item in the front of the deque
     *
     * @param item the item to be added
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Input is null");
        }
        if (this.capacity == size()) {
            resize(2 * this.capacity);
        }
        if (isEmpty()) {
            this.deq[this.first] = item;
            this.numberOfItems++;
            return;
        }
        this.first = dec(this.first);
        this.deq[this.first] = item;
        this.numberOfItems++;

    }

    /**
     * adds an item at the back of the deque
     *
     * @param item the item to be added
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Input is null");
        }
        if (this.capacity == size()) {
            resize(2 * this.capacity);
        }
        if (isEmpty()) {
            this.deq[this.first] = item;
            this.numberOfItems++;
            return;
        }
        this.last = inc(this.last);
        this.deq[this.last] = item;
        this.numberOfItems++;

    }

    /**
     * removes the first item in the deque
     *
     * @return the removed item in the deque
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size() == 1) {
            Item temp = this.deq[this.first];
            this.deq[this.first] = null;
            this.numberOfItems--;
            return temp;
        }
        if ((this.capacity / 4) > size()) {
            resize(this.capacity / 2);
        }
        Item temp = this.deq[this.first];
        this.deq[this.first] = null;
        this.first = inc(this.first);
        this.numberOfItems--;
        return temp;
    }

    /**
     * removes the last item in the deque
     *
     * @return the removed item in the deque
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size() == 1) {
            Item temp = this.deq[this.first];
            this.deq[this.first] = null;
            this.numberOfItems--;
            return temp;
        }
        if ((this.capacity / 4) >= size()) {
            resize(this.capacity / 2);
        }
        Item temp = this.deq[this.last];
        this.deq[this.last] = null;
        this.last = dec(this.last);
        this.numberOfItems--;
        return temp;
    }

    /**
     * Returns an iterator over elements of type {@code Item}.
     *
     * @return an Iterator.
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    /**
     * resizes the array when it is full
     */
    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        for (int i = 0; i < this.numberOfItems; i++) {
            temp[i] = this.deq[this.first];
            this.first = inc(this.first);
        }
        this.first = 0;
        this.last = this.numberOfItems - 1;
        this.capacity = cap;
        this.deq = temp;

    }

    /**
     * increments the pointers and make them behave like a cirlce
     *
     * @param num the number to be incremented
     * @return the incremented value behaving like a circle
     */
    private int inc(int num) {
        num++;
        return ((num % this.capacity) + this.capacity) % this.capacity;
    }

    /**
     * decrements the number and makes it behave like a circle
     *
     * @param num the number to be decremented
     * @return the decremented number behaving like a circle
     */
    private int dec(int num) {
        num--;
        return ((num % this.capacity) + this.capacity) % this.capacity;
    }

    /**
     * unit testing
     *
     * @param args arguments for testing if necessary
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        System.out.println(deque.isEmpty());
        System.out.println(deque.isEmpty());
        System.out.println(deque.isEmpty());
        System.out.println(deque.isEmpty());
        deque.addFirst(5);
        deque.addFirst(3);
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.isEmpty());
    }

    private class DequeIterator implements Iterator<Item> {
        private int current = first - 1;
        private int tracker = 0;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return tracker != numberOfItems;
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
            this.current = inc(this.current);
            tracker++;
            return deq[this.current];
        }
    }

}
