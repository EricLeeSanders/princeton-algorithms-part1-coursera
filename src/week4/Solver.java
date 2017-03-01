package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private int moves;
        private SearchNode prevSearchNode;
        private boolean twin;

        public SearchNode(Board board, int moves, SearchNode prevSearchNode, boolean twin) {

            this.board = board;
            this.moves = moves;
            this.prevSearchNode = prevSearchNode;
            this.twin = twin;
        }

        public int priority() {

            return board.manhattan() + moves;
        }

        public int getMoves() {

            return moves;
        }

        public Board getBoard() {

            return board;
        }

        public SearchNode getPrevSearchNode() {

            return prevSearchNode;
        }

        public boolean isTwin() {

            return twin;
        }

        @Override
        public int compareTo(SearchNode s) {

            return this.priority() - s.priority();
        }

    }

    private SearchNode solutionSearchNode;

    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {

        if (initial == null) {
            throw new NullPointerException();
        }

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        SearchNode initialSearchNode = new SearchNode(initial, 0, null, false);
        SearchNode initialSearchNodeTwin = new SearchNode(initial.twin(), 0, null, true);

        pq.insert(initialSearchNode);
        pq.insert(initialSearchNodeTwin);

        while (!pq.isEmpty()) {
            SearchNode sn = pq.delMin();
            Board b = sn.getBoard();

            if (b.isGoal()) {
                if (!sn.isTwin()) {
                    solutionSearchNode = sn;
                }
                return;
            }

            SearchNode prevSearchNode = sn.getPrevSearchNode();
            for (Board neighbor : b.neighbors()) {
                if (prevSearchNode == null || !neighbor.equals(prevSearchNode.getBoard())) {
                    SearchNode neighborNode = new SearchNode(neighbor, sn.moves + 1, sn, sn.isTwin());
                    pq.insert(neighborNode);
                }
            }

        }

    }

    /**
     * Determines if the board is solvable
     * 
     * @return
     */
    public boolean isSolvable() {

        return solutionSearchNode != null;
    }

    /**
     * minimum number of moves to solve initial board; -1 if unsolvable
     * 
     * @return
     */
    public int moves() {

        return isSolvable() ? solutionSearchNode.getMoves() : -1;
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * 
     * @return
     */
    public Iterable<Board> solution() {

        if (!isSolvable()) {

            return null;
        }

        Stack<Board> stack = new Stack<Board>();
        SearchNode sn = solutionSearchNode;
        stack.push(sn.getBoard());
        SearchNode prevSn = sn.getPrevSearchNode();
        while (prevSn != null) {
            stack.push(prevSn.getBoard());
            prevSn = prevSn.getPrevSearchNode();
        }

        return stack;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
            StdOut.println("Minimum number of moves = " + solver.moves());
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
