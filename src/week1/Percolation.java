package week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * We model a percolation system using an n-by-n grid of sites. Each site is
 * either open or blocked. A full site is an open site that can be connected to
 * an open site in the top row via a chain of neighboring (left, right, up,
 * down) open sites. We say the system percolates if there is a full site in the
 * bottom row. In other words, a system percolates if we fill all open sites
 * connected to the top row and that process fills some open site on the bottom
 * row.
 * 
 * @author Eric
 *
 */
public class Percolation {

    private static final byte CLOSED = 0;
    private static final byte OPEN = 1;
    private static final byte CONNECTED_TOP = 2;
    private static final byte CONNECTED_BOTTOM = 4;
    private static final byte CONNECTED_BOTH = 8;

    private WeightedQuickUnionUF uf;

    private byte[] status;
    private int n;
    private int numOfOpenSites;
    private int size;
    private boolean percolates;

    /**
     * Creates an n*n grid
     * 
     * @param n
     */
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }

        size = n * n;
        this.n = n;
        status = new byte[size + 1];
        uf = new WeightedQuickUnionUF(size + 1);
    }

    /**
     * Opens a site if it is not open already
     * 
     * @param row
     * @param col
     */
    public void open(int row, int col) {

        validate2DCoords(row, col);

        int site = twoDimToOneDim(row, col);

        if (status[site] != CLOSED) {
            return;
        }

        byte state = 0;
        numOfOpenSites++;

        // Connect to virtual top site if row 1
        if (row == 1) {
            state += CONNECTED_TOP;
        }

        // Connect to virtual bottom site if last row
        if (row == n) {
            state += CONNECTED_BOTTOM;
        }
        // Connect up
        if (row > 1) {
            int siteUp = twoDimToOneDim(row - 1, col);
            if (status[siteUp] != CLOSED) {
                state |= status[uf.find(siteUp)];
                uf.union(site, siteUp);
            }
        }

        // Connect down
        if (row < n) {
            int siteDown = twoDimToOneDim(row + 1, col);
            if (status[siteDown] != CLOSED) {
                state |= status[uf.find(siteDown)];
                uf.union(site, siteDown);
            }
        }

        // Connect left
        if (col > 1) {
            int siteLeft = twoDimToOneDim(row, col - 1);
            if (status[siteLeft] != CLOSED) {
                state |= status[uf.find(siteLeft)];
                uf.union(site, siteLeft);
            }
        }

        // Connect Right
        if (col < n) {
            int siteRight = twoDimToOneDim(row, col + 1);
            if (status[siteRight] != CLOSED) {
                state |= status[uf.find(siteRight)];
                uf.union(site, siteRight);
            }
        }

        int siteRoot = uf.find(site);
        status[siteRoot] = OPEN;
        status[site] = OPEN;

        if ((state & CONNECTED_BOTH) == CONNECTED_BOTH) {
            status[siteRoot] = CONNECTED_BOTH;
        } else if ((state & CONNECTED_TOP) == CONNECTED_TOP && (state & CONNECTED_BOTTOM) == CONNECTED_BOTTOM) {
            status[siteRoot] = CONNECTED_BOTH;
        } else if ((state & CONNECTED_TOP) == CONNECTED_TOP) {
            status[siteRoot] = CONNECTED_TOP;
        } else if ((state & CONNECTED_BOTTOM) == CONNECTED_BOTTOM) {
            status[siteRoot] = CONNECTED_BOTTOM;
        }

        if (status[siteRoot] == CONNECTED_BOTH) {
            percolates = true;
        }

    }

    /**
     * Determines if a site is open
     * 
     * @param row
     * @param col
     * 
     * @return if site is open
     */
    public boolean isOpen(int row, int col) {

        validate2DCoords(row, col);

        int site = twoDimToOneDim(row, col);

        return status[site] != CLOSED;
    }

    /**
     * Determines if a site is full
     * 
     * @param row
     * @param col
     * 
     * @return if site is full
     */
    public boolean isFull(int row, int col) {

        validate2DCoords(row, col);

        int site = twoDimToOneDim(row, col);
        int root = uf.find(site);

        return status[root] == CONNECTED_TOP || status[root] == CONNECTED_BOTH;
    }

    /**
     * Gets the number of open sites
     * 
     * @return number of open sites
     */
    public int numberOfOpenSites() {

        return numOfOpenSites;
    }

    /**
     * Determines if the system percolates
     * 
     * @return if system percolates
     */
    public boolean percolates() {

        return percolates;
    }

    /**
     * Converts 2D coords into 1D
     * 
     * @param row
     * @param col
     * 
     * @return 1D coords
     */
    private int twoDimToOneDim(int row, int col) {

        return (row - 1) * n + col;
    }

    /**
     * Validates a coordinate
     * 
     * @param row
     * @param col
     */
    private void validate2DCoords(int row, int col) {

        if (row > n || col > n || row <= 0 || col <= 0) {
            throw new IndexOutOfBoundsException("Row or Col out of bounds");
        }
    }
}
