/* *****************************************************************************
 *  Name:   Bisrat Zerihun
 *  Date:   07/01/2021
 *  Description:    Solves the 8puzzle game efficiently and returns
 *                  the number of moves needed to solve it
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private class Node implements Comparable<Node> {
        private int moves;
        private Board result;
        private Node prev;

        public Node(Board board) {
            this.moves = 0;
            this.prev = null;
            this.result = board;
        }

        public Node(Board board, int moves, Node prev) {
            this.moves = moves;
            this.result = board;
            this.prev = prev;
        }

        public int priority() {
            int man = this.result.manhattan();
            return this.moves + man;
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure
         * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
         * for all {@code x} and {@code y}.  (This
         * implies that {@code x.compareTo(y)} must throw an exception iff
         * {@code y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
         * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
         * all {@code z}.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
         * <i>signum</i> function, which is defined to return one of {@code -1},
         * {@code 0}, or {@code 1} according to whether the value of
         * <i>expression</i> is negative, zero, or positive, respectively.
         *
         * @param that the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws IllegalArgumentException if the specified object is null
         */
        public int compareTo(Node that) {
            if (that == null) throw new IllegalArgumentException("NUll argument");
            return Integer.compare(this.priority(), that.priority());
        }
    }

    private boolean solvable = false;
    private MinPQ<Node> moves;

    /**
     * finds a solution for the given board
     *
     * @param initial the given board
     */
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        moves = new MinPQ<Node>();
        MinPQ<Node> twinMoves = new MinPQ<Node>();

        moves.insert(new Node(initial));
        twinMoves.insert(new Node(initial.twin()));
        // two nodes incase the puzzle is unsolvable
        Node sol;
        Node twinSol;
        while (!twinMoves.isEmpty() && !moves.isEmpty()) {
            // check if board is unsolvable and use twin board if not
            if (moves.min().result.isGoal()) {
                this.solvable = true;
                return;
            } else if (twinMoves.min().result.isGoal()) {
                this.solvable = false;
                return;
            }

            sol = moves.delMin();
            twinSol = twinMoves.delMin();

            Iterable<Board> neighbors = sol.result.neighbors();
            Iterable<Board> twinNeighbors = twinSol.result.neighbors();

            for (Board n : neighbors) {
                if (sol.prev == null || !n.equals(sol.prev.result)) {
                    moves.insert(new Node(n, sol.moves + 1, sol));
                }

            }
            for (Board n : twinNeighbors) {
                if (twinSol.prev == null || !n.equals(twinSol.prev.result)) {
                    twinMoves.insert(new Node(n, twinSol.moves + 1, twinSol));
                }
            }


        }

    }

    /**
     * checks if the board is solvable
     *
     * @return true if it is and false if not
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * the min number of moves to solve the inital board; -1 if
     * unsolvable
     *
     * @return the min number of moves needed(-1 if impossible)
     */
    public int moves() {
        if (isSolvable()) return moves.min().moves;
        return -1;
    }

    /**
     * @return sequence of boards in a shortest solution;
     * null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solutions = new Stack<Board>();
        Node cur = moves.min();
        // the solution will be a linked list starting
        // from the last min then goes up
        while (cur.prev != null) {
            solutions.push(cur.result);
            cur = cur.prev;
        }
        solutions.push(cur.result);
        return solutions;
    }

    public static void main(String[] args) {
        // // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         tiles[i][j] = in.readInt();
        // Board initial = new Board(tiles);
        //
        // // solve the puzzle
        // Solver solver = new Solver(initial);
        //
        // // print solution to standard output
        // if (!solver.isSolvable())
        //     StdOut.println("No solution possible");
        // else {
        //     StdOut.println("Minimum number of moves = " + solver.moves());
        //     for (Board board : solver.solution())
        //         StdOut.println(board);
        // }
    }
}
