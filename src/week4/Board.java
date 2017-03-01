package week4;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;

public class Board {

    private final int n;
    private final int[] array;
    private int blankIndex;
    private int hammingNum;
    private int manhattanNum;
    private boolean goal;

    /**
     * constructs a board from an n-by-n array of blocks
     * 
     * @param blocks
     */
    public Board(int[][] blocks) {

        n = blocks.length;
        array = new int[n * n];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int index = convert2dTo1d(i, j);
                array[index] = blocks[i][j];
                if (array[index] == 0) {
                    blankIndex = index;
                }
            }
        }

        calculateHamming();
        calculateManhattan();
        calculateIsGoal();
    }

    /**
     * Creates a 2d array from the 1d array
     * 
     */
    private int[][] create2dArray() {

        int[][] array2d = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = (i * n) + j;
                array2d[i][j] = array[index];
            }
        }

        return array2d;
    }

    /**
     * Calculates the Hamming priority
     */
    private void calculateHamming() {

        for (int i = 0; i < array.length; i++) {
            if (array[i] != (i + 1) && array[i] != 0) {
                hammingNum++;
            }
        }
    }

    /**
     * Calculates the Manhattan priority
     */
    private void calculateManhattan() {

        for (int i = 0; i < array.length; i++) {

            if (array[i] == 0) {
                continue;
            }
            int col = convert1dTo2dCol(array[i] - 1);
            int row = convert1dTo2dRow(array[i] - 1);

            int goalCol = convert1dTo2dCol(i);
            int goalRow = convert1dTo2dRow(i);

            manhattanNum += Math.abs(col - goalCol);
            manhattanNum += Math.abs(row - goalRow);

        }
    }

    /**
     * Determines if this board is the goal
     */
    private void calculateIsGoal() {

        for (int i = 0; i < array.length; i++) {
            if (array[i] != (i + 1) && array[i] != 0) {
                goal = false;
                return;
            }
        }

        goal = true;
    }

    /**
     * Converts a 1d coordinate to a 2d row coordinate
     * 
     * @param index
     */
    private int convert1dTo2dRow(int index) {

        return (index / n);
    }

    /**
     * Converts a 1d coordinate to a 2d column coordinate
     * 
     * @param index
     */
    private int convert1dTo2dCol(int index) {

        return index - ((convert1dTo2dRow(index)) * n);
    }

    /**
     * Converts a 2d coordinate into a 1d
     * 
     * @param row
     * @param col
     */
    private int convert2dTo1d(int row, int col) {

        return (col + (n * row));
    }

    /**
     * Creates the neighbor boards for this board
     */
    private List<Board> createNeighborBoards() {

        List<Board> neighborBoards = new ArrayList<Board>();

        int col = convert1dTo2dCol(blankIndex);
        int row = convert1dTo2dRow(blankIndex);

        // Move blank space up
        if (row > 0) {
            neighborBoards.add(createNeighborBoard(-n));
        }

        // Move space down
        if (row < (n - 1)) {
            neighborBoards.add(createNeighborBoard(n));
        }

        // move space right
        if (col < (n - 1)) {
            neighborBoards.add(createNeighborBoard(1));
        }

        // move space left
        if (col > 0) {
            neighborBoards.add(createNeighborBoard(-1));
        }

        return neighborBoards;

    }

    /**
     * Creates a neighbor board
     * 
     * @param pos
     */
    private Board createNeighborBoard(int pos) {

        swap(blankIndex + pos, blankIndex);
        Board neighbor = new Board(create2dArray());
        swap(blankIndex + pos, blankIndex);
        return neighbor;
    }

    /**
     * Swaps two entries in the array
     * 
     * @param i
     * @param j
     */
    private void swap(int i, int j) {

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Creates the twin board
     */
    private Board createTwinBoard() {

        int first, second;
        if (array[0] != 0 && array[1] != 0) {
            first = 0;
            second = 1;
        } else {
            first = convert2dTo1d(1, 0);
            second = convert2dTo1d(1, 1);
        }

        swap(first, second);
        Board twinBoard = new Board(create2dArray());
        swap(first, second);

        return twinBoard;
    }

    /**
     * Board dimension n
     */
    public int dimension() {

        return n;
    }

    /**
     * The number of blocks out of place
     * 
     * @return
     */
    public int hamming() {

        return hammingNum;
    }

    /**
     * Sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {

        return manhattanNum;
    }

    /**
     * If the board the goal board?
     */
    public boolean isGoal() {

        return goal;
    }

    /**
     * A twin board that is obtained by exchanging any pair of blocks
     * 
     * @return
     */
    public Board twin() {

        Board twin = createTwinBoard();
        return twin;
    }

    /**
     * Compares this board to the parameter board.
     */
    public boolean equals(Object o) {

        if (!(o instanceof Board)) {
            return false;
        }

        Board b = (Board) o;

        if (b.dimension() != this.dimension()) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] != b.array[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * All neighboring boards
     */
    public Iterable<Board> neighbors() {

        List<Board> neighborBoards = createNeighborBoards();
        return neighborBoards;
    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = convert2dTo1d(i, j);
                s.append(String.format("%2d ", array[index]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        initial.neighbors();
        initial.twin();
        System.out.println(initial);
    }
}
