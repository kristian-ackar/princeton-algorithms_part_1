import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.io.IOException;

/**
 * Percolation data type using the weighted quick union union-find algorithm
 */
public class Percolation {
    private final int n;
    private boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final int topId, bottomId;
    private int openSites;

    /**
     * Creates n-by-n grid, with all sites initially blocked
     * @param n
     * @throws IllegalArgumentException
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be greater then 0");

        this.n = n;
        grid = new boolean[n][n];
        topId = 0;
        bottomId = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    /**
     * Opens the site (row, col) if it is not open already
     * @param row
     * @param col
     * @throws IllegalArgumentException
     */
    public void open(int row, int col) {
        checkBoundaries(row, col);

        // If site already opened return
        if (isOpen(row, col)) return;

        // Open site
        grid[row - 1][col - 1] = true;
        openSites++;

        // Connect with virtual top or bottom
        if (row == 1) uf.union(getIndex(row, col), topId);
        else if (row == n) uf.union(getIndex(row, col), bottomId);

        // Connect with open neighbour sites
        // Left neighbour
        if (col > 1 && isOpen(row, col - 1)) uf.union(getIndex(row, col - 1), getIndex(row, col));

        // Right neighbour
        if (col < n && isOpen(row, col + 1)) uf.union(getIndex(row, col + 1), getIndex(row, col));

        // Upper neighbour
        if (row > 1 && isOpen(row - 1, col)) uf.union(getIndex(row - 1, col), getIndex(row, col));

        // Lower neighbour
        if (row < n && isOpen(row + 1, col)) uf.union(getIndex(row + 1, col), getIndex(row, col));
    }

    /**
     * Is the site (row, col) open?
     * @param row
     * @param col
     * @return
     * @throws IllegalArgumentException
     */
    public boolean isOpen(int row, int col) {
        checkBoundaries(row, col);

        return grid[row - 1][col - 1];
    }

    /**
     * Is the site (row, col) full?
     * @param row
     * @param col
     * @return
     * @throws IllegalArgumentException
     */
    public boolean isFull(int row, int col) {
        checkBoundaries(row, col);

        // Translate 2D indices to 1D index
        int id = getIndex(row, col);

        return uf.find(id) == uf.find(topId);
    }

    /**
     * Returns the number of open sites
     * @return
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Does the system percolate?
     * @return
     */
    public boolean percolates() {
        return uf.find(topId) == uf.find(bottomId);
    }

    /**
     * Translate 2D indices to 1D index
     * @param row
     * @param col
     * @return
     * @throws IllegalArgumentException
     */
    private int getIndex(int row, int col) {
        checkBoundaries(row, col);

        return  (row - 1) * n + col;
    }

    /**
     * Check are row and col params inside boundaries
     * @param row
     * @param col
     * @throws IllegalArgumentException
     */
    private void checkBoundaries(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException(String.format("Row and column indices must be between 1 and %d", n));
    }
}
