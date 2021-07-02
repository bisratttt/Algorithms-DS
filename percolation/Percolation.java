import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name:              Bisrat Zerihun
 *  Coursera User ID:
 *  Last modified:     06/04/2021
 **************************************************************************** */
public class Percolation {
    private boolean[][] arr;
    private WeightedQuickUnionUF full;
    private WeightedQuickUnionUF perc;
    private int N;
    private int openSites;
    private int virtualTop;
    private int virtualBottom;

    /**
     * creates the percolation program where it blocks out every grid initially
     *
     * @param n the number of grids on every row/column
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("The N value has to be grater than 0");
        }
        this.N = n;
        this.virtualTop = 0;
        this.virtualBottom = (n * n) + 1;
        this.full = new WeightedQuickUnionUF((n * n) + 1);
        this.perc = new WeightedQuickUnionUF((n * n) + 2);

        arr = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = false;
            }
        }
        for (int i = 1; i <= n; i++) {
            full.union(this.virtualTop, toInt(1, i));
            perc.union(this.virtualTop, toInt(1, i));
            perc.union(this.virtualBottom, toInt(this.N, i));
        }

    }

    /**
     * opens a grid on the plane after checking whether each neighboring grid is open or not
     *
     * @param row x value of the grid
     * @param col y value of the grid
     */
    public void open(int row, int col) {
        if (row > 1 && isOpen(row - 1, col)) {
            full.union(toInt(row - 1, col), toInt(row, col));
            perc.union(toInt(row - 1, col), toInt(row, col));
        }
        if (row < N && isOpen(row + 1, col)) {
            full.union(toInt(row + 1, col), toInt(row, col));
            perc.union(toInt(row + 1, col), toInt(row, col));

        }
        if (col > 1 && isOpen(row, col - 1)) {
            full.union(toInt(row, col - 1), toInt(row, col));
            perc.union(toInt(row, col - 1), toInt(row, col));
        }
        if (col < N && isOpen(row, col + 1)) {
            full.union(toInt(row, col + 1), toInt(row, col));
            perc.union(toInt(row, col + 1), toInt(row, col));
        }

        if (!arr[row - 1][col - 1]) {
            this.openSites++;
        }
        arr[row - 1][col - 1] = true;
    }

    /**
     * checks if a grid is open or not open
     *
     * @param row the x value of the grid
     * @param col the y value of the grid
     * @return true if open and false if not
     */
    public boolean isOpen(int row, int col) {
        validateCorners(row, col);
        return arr[row - 1][col - 1];
    }

    /**
     * checks if the grid is full or not by checking its connection with the top node
     *
     * @param row x value of the grid
     * @param col y value of the grid
     * @return true if full and false if not
     */
    public boolean isFull(int row, int col) {
        validateCorners(row, col);
        return isOpen(row, col) && full.find(toInt(row, col)) == full.find(this.virtualTop);
    }

    /**
     * returns the number of open sites for the game
     *
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return this.openSites;
    }

    /**
     * checks if the plane percolates or not by checking if the top and bottom node are connected
     *
     * @return true if percolates and false otherwise
     */
    public boolean percolates() {
        if (this.N == 1) {
            return isOpen(1, 1);
        }
        return perc.find(this.virtualBottom) == perc.find(this.virtualTop);
    }

    /**
     * checks if the x and y value of a grid is inside the plane
     *
     * @param row x value of the grid
     * @param col y value of the grid
     */
    private void validateCorners(int row, int col) {
        if (row < 1 || row > this.N || col < 1 || col > this.N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /**
     * changes the grid into x and y value into a consecutive number
     *
     * @param row x value of the grid
     * @param col y value of the grid
     * @return the number value of the coordinates
     */
    private int toInt(int row, int col) {
        return (this.N * (row - 1)) + col;
    }

    /**
     * place to run client code on the program
     *
     * @param args can have the size of the plane
     */
    public static void main(String[] args) {

    }

}
