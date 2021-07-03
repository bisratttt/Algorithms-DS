/* *****************************************************************************
 *  Name:   Bisrat Zerihun
 *  Date:   06/29/2021
 *  Description:    The 8puzzle game board that has all the properties as
 *                  the real game
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private int n;
    private int[][] tiles;
    private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private int hamming = -1;
    private int manhattan = -1;

    /**
     * creates an n-by-n board for the game to be played
     *
     * @param tiles matrix that acts as the board with n rows and cols
     */
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("argument cannot be null");
        this.n = tiles[0].length;
        // be careful on the implementation of the copy, make sure it does not use a lot
        // of memory
        this.tiles = copyArr(tiles);
    }

    /**
     * string representation of the board
     *
     * @return string representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.n + "");
        sb.append("\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(this.tiles[i][j]);
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @return the board dimension n
     */
    public int dimension() {
        return this.n;
    }

    /**
     * keeps track of the number of tiles that are
     * out of place
     *
     * @return the number of tiles out of place
     */
    public int hamming() {
        if (hamming != -1) return hamming;
        int ham = 0;
        // definition of hamming
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != findGoal(i, j)) {
                    ham++;
                }
            }
        }
        if (this.tiles[dimension() - 1][dimension() - 1] != 0) ham--;
        this.hamming = ham;
        return this.hamming;
    }

    /**
     * @return sum of manhattan distances between
     * the tiles and goal
     */
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        int sum = 0;
        // uses the manhattan formula for two points
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != findGoal(i, j) && this.tiles[i][j] != 0) {
                    int[] coords = findCoordinates(this.tiles[i][j]);
                    sum += Math.abs(i - coords[0]) + Math.abs(j - coords[1]);
                }
            }
        }
        this.manhattan = sum;
        return this.manhattan;
    }

    /**
     * is this board the goal board
     *
     * @return true if goal board and false
     * otherwise
     */
    public boolean isGoal() {
        boolean isEqual = true;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != findGoal(i, j)) {
                    isEqual = false;
                }
            }
        }
        return isEqual;
    }

    /**
     * checks if two boards are equal
     *
     * @param y the board to be checked for equality
     * @return true if board is equal
     */
    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++) {
            if (!Arrays.equals(this.tiles[i], that.tiles[i])) return false;
        }
        return true;
    }

    /**
     * all neighboring boards
     *
     * @return the neighboring boards
     */
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] == 0) {
                    for (int[] dir : DIRS) {
                        // calculates neighboring points
                        int x = dir[0] + i;
                        int y = dir[1] + j;
                        if (x < dimension() && x >= 0 && y < dimension() && y >= 0) {
                            int[][] tempTiles = copyArr(this.tiles);
                            exch(tempTiles, i, j, x, y);
                            neighbors.add(new Board(tempTiles));
                        }
                    }
                    return neighbors;
                }
            }
        }
        return neighbors;
    }

    /**
     * a board that is obtained by exchanging a pair of tiles
     *
     * @return the board result after exchange
     */
    public Board twin() {
        // this method is needed for the solver later on
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != 0) {
                    for (int[] dir : DIRS) {
                        // similar to the neighbors method
                        int x = dir[0] + i;
                        int y = dir[1] + j;
                        if (x < dimension() && x >= 0 && y < dimension() && y >= 0
                                && this.tiles[x][y] != 0) {
                            int[][] tempTiles = copyArr(this.tiles);
                            exch(tempTiles, i, j, x, y);
                            return new Board(tempTiles);
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * finds the appropriate cols for given number
     *
     * @param num the number where coordinates will be calculated
     * @return the correct coordinates for the given number
     */
    private int[] findCoordinates(int num) {
        int x = (num - 1) / dimension();
        int y = (num - 1) % dimension();
        return new int[]{x, y};
    }

    /**
     * finds the goal value for the respective row and col
     *
     * @param row the input row of tile
     * @param col the input col of tile
     * @return the appropriate value for that tile
     */
    private int findGoal(int row, int col) {
        return ((row * dimension()) + col + 1) % (dimension() * dimension());
    }

    /**
     * exchanges a pair of tiles from the given 2d array
     *
     * @param arr   the given array where exchange happens
     * @param row   first tile row value
     * @param col   first tile col value
     * @param exRow second tile row value
     * @param exCol second tile col value
     */
    private void exch(int[][] arr, int row, int col, int exRow, int exCol) {
        int temp = arr[row][col];
        arr[row][col] = arr[exRow][exCol];
        arr[exRow][exCol] = temp;
    }

    /**
     * copies a 2d array
     *
     * @param arr array to be copied
     * @return copied array
     */
    private int[][] copyArr(int[][] arr) {
        int[][] tempTiles = new int[dimension()][dimension()];
        for (int m = 0; m < dimension(); m++)
            for (int n = 0; n < dimension(); n++) {
                tempTiles[m][n] = arr[m][n];
            }
        return tempTiles;
    }

    /**
     * tester code
     *
     * @param args file of tiles
     */
    public static void main(String[] args) {
        // // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         tiles[i][j] = in.readInt();
        // Board initial = new Board(tiles);
        // StdOut.println(initial.toString());
        // for (Board nen : initial.neighbors()) {
        //     StdOut.println(nen.toString());
        // }
        // // for (int i = 0; i < 30; i++) {
        // //     Board twin = initial.twin();
        // //     StdOut.println(twin.toString());
        // //     StdOut.println(twin.hamming());
        // //     StdOut.println(twin.manhattan());
        // // }
        // StdOut.println(initial.hamming());
        // StdOut.println(initial.isGoal());
        // StdOut.println(initial.manhattan());

    }

}
