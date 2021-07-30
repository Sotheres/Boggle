import edu.princeton.cs.algs4.TrieST;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final TrieST<Integer> dictionary;
    private final Set<String> validWords;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TrieST<>();
        for (String s : dictionary) {
            this.dictionary.put(s, 1);
        }
        validWords = new HashSet<>();
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();

        for (int i = 0; i < rows * cols; i++) {
            new DFS(rows, cols, i);
        }
    }

    public int scoreOf(String word) {

    }

    private class DFS {

        private boolean[] marked;
        private int[] edgeTo;
        private int s;
        private final int numOfRows;
        private final int numOfCols;

        public DFS(int rows, int cols, int s) {
            this.numOfRows = rows;
            this.numOfCols = cols;
            marked = new boolean[rows * cols];
            edgeTo = new int[rows * cols];
            this.s = s;
            runDfs(s);
        }

        private void runDfs(int v) {
            marked[v] = true;
            for (int w : adjacent(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    runDfs(w);
                }
            }
        }

        private int[] adjacent(int f) {
            int[] adj;

            if (f == 0) {
                adj = new int[]{f + 1,
                                f + numOfCols,
                                f + numOfCols + 1};
            } else if (f < numOfCols - 1) {
                adj = new int[]{f - 1,
                                f + 1,
                                f + numOfCols - 1,
                                f + numOfCols,
                                f + numOfCols + 1};
            } else if (f == numOfCols - 1) {
                adj = new int[]{f - 1,
                                f + numOfCols - 1,
                                f + numOfCols};
            } else if ((f % numOfCols == 0) && (f != (numOfCols - 1) * numOfRows)) {
                adj = new int[]{f - numOfCols,
                                f - numOfCols + 1,
                                f + 1,
                                f + numOfCols,
                                f + numOfCols + 1};
            } else if ((f % numOfCols != 0) && (f % (numOfCols - 1) != 0)) {
                adj = new int[]{f - numOfCols - 1,
                                f - numOfCols,
                                f - numOfCols + 1,
                                f - 1,
                                f + 1,
                                f + numOfCols - 1,
                                f + numOfCols,
                                f + numOfCols + 1};
            } else if ((f % numOfCols - 1 == 0) && (f != numOfCols * numOfRows - 1)) {
                adj = new int[]{f - numOfCols - 1,
                                f - numOfCols,
                                f - 1,
                                f + numOfCols - 1,
                                f + numOfCols};
            } else if (f == (numOfCols - 1) * numOfRows) {
                adj = new int[]{f - numOfCols,
                                f - numOfCols + 1,
                                f + 1};
            } else if ((f != (numOfCols - 1) * numOfRows) && (f != numOfCols * numOfRows - 1)) {
                adj = new int[]{f - numOfCols - 1,
                                f - numOfCols,
                                f - numOfCols + 1,
                                f - 1,
                                f + 1};
            } else {
                adj = new int[]{f - numOfCols - 1,
                                f - numOfCols,
                                f - 1};
            }

            return adj;
        }
    }
}
