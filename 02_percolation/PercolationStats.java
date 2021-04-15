import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double[] openedSitesPerc;
    /**
     * Perform independent trials on an n-by-n grid
     * @param n
     * @param trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Grid size must be greater then 0");

        this.trials = trials;
        openedSitesPerc = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                percolation.open(row, col);
            }

            openedSitesPerc[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    /**
     * Sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(openedSitesPerc);
    }

    /**
     * Sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return StdStats.stddev(openedSitesPerc);
    }

    /**
     * Low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    /**
     * High endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.println(String.format("mean                     = %f", percolationStats.mean()));
        StdOut.println(String.format("stddev                   = %f", percolationStats.stddev()));
        StdOut.println(String.format("95%% confidence interval = [%f, %f]", percolationStats.confidenceLo(), percolationStats.confidenceHi()));
    }
}
