package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Performs tests on {@link Percolation.java}
 * 
 * @author Eric
 *
 */
public class PercolationStats {

    private double[] trials;

    /**
     * perform trials on an n-by-n grid
     * 
     * @param n
     * @param numOfTrials
     */
    public PercolationStats(int n, int numOfTrials) {

        if (n <= 0 || numOfTrials <= 0) {
            throw new IllegalArgumentException(" n and number of trials must be greater than 0");
        }
        trials = new double[numOfTrials];

        for (int i = 0; i < numOfTrials; i++) {
            Percolation p = new Percolation(n);
            int openSites = 0;
            while (!p.percolates()) {
                int rndRow = StdRandom.uniform(n) + 1;
                int rndCol = StdRandom.uniform(n) + 1;
                if (!p.isOpen(rndRow, rndCol)) {
                    p.open(rndRow, rndCol);
                    openSites++;
                }
            }
            trials[i] = ((double) openSites) / ((double) n * n);

        }
    }

    /**
     * Sample mean of percolation threshold
     * 
     */
    public double mean() {

        return StdStats.mean(trials);
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {

        return StdStats.stddev(trials);
    }

    /**
     * low endpoint of 95% confidence interval
     */
    public double confidenceLo() {

        return mean() - ((1.96 * stddev()) / Math.sqrt(trials.length));
    }

    /**
     * high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials.length));
    }

    public static void main(String[] args) {

        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
