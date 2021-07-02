import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/* *****************************************************************************
 *  Name:              Bisrat Zerihun
 *  Coursera User ID:
 *  Last modified:     06/09/2021
 **************************************************************************** */
public class PercolationStats {
    private double[] results;
    private int trials;

    /**
     * gives stats on the percolation threshold by intaking different size items and trials
     *
     * @param n      the size of the percolation visual
     * @param trials the number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and trials must be positive natural numbers!");
        }
        this.trials = trials;
        results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                perc.open(x, y);
            }
            results[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

    }

    /**
     * finds the mean of all the percolation open sites from different trials
     *
     * @return the mean value for different percolation sites on different visuals
     */
    public double mean() {
        return StdStats.mean(this.results);
    }

    /**
     * finds the standard deviation on different trails of the percolation simulation
     *
     * @return the standard deviation
     */
    public double stddev() {
        return StdStats.stddev(this.results);
    }

    /**
     * checks the 95% confidence interval, this is the confidence low
     *
     * @return the confidence low
     */
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(this.trials));
    }

    /**
     * checks the 95% confidence interval, this is the confidence high
     *
     * @return the confidence high
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(this.trials));
    }

    /**
     * runs the statistics and prints the results for each value
     *
     * @param args the size and trails to run the program
     */
    public static void main(String[] args) {
        int sideLength = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(sideLength, trials);
        StdOut.printf("mean\t=%f%n", test.mean());
        StdOut.printf("stddev\t=%f%n", test.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]%n", test.confidenceLo(),
                      test.confidenceHi());
    }
}
