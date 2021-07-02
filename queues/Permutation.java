/* *****************************************************************************
 *  Name: Bisrat Zerihun
 *  Date: 06/16/2021
 *  Description: client code for the queues
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int trials = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {

            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < trials; i++) {
            StdOut.printf("%s \n", queue.dequeue());
        }
    }
}
