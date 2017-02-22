package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Takes a command-line integer k; reads in a sequence of strings from standard
 * input; prints exactly k of them, uniformly at random. Uses Reservoir Sampling
 * to use less space.
 * 
 * @author Eric
 *
 */
public class Permutation {


    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);
        int count = 0;

        if (k == 0) {
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {

            String s = StdIn.readString();
            if (count < k) {
                // add item to rq
                rq.enqueue(s);
            } else {
                // determine if an item should in rq
                // should be replaced or not
                int j = StdRandom.uniform(count + 1);
                if (j < k) {
                    // Dequeue a random item
                    rq.dequeue();
                    // enqueue the new item
                    rq.enqueue(s);
                }

            }
            count++;
        }

        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }

    }

}
