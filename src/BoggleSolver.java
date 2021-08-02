import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.time.Duration;
import java.time.Instant;
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
            new DFS(rows, cols, board, i);
        }

        return validWords;
    }

    public int scoreOf(String word) {
        if (!dictionary.contains(word)) {
            return 0;
        }
        if (3 <= word.length() && word.length() <= 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            System.out.println(word);
            score += solver.scoreOf(word);
        }
        System.out.println("Score = " + score);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);
    }

    private class DFS {

        private final boolean[] marked;
        private final int numOfRows;
        private final int numOfCols;
        private final BoggleBoard board;
        private final StringBuilder currentWord;

        public DFS(int rows, int cols, BoggleBoard board, int s) {
            this.numOfRows = rows;
            this.numOfCols = cols;
            marked = new boolean[rows * cols];
            this.board = board;
            currentWord = new StringBuilder();
            runDfs(s);
        }

        private void runDfs(int v) {
            marked[v] = true;

            char ch = board.getLetter((v / numOfCols) % numOfCols, v % numOfCols);
            if (ch == 'Q') {
                currentWord.append("QU");
            } else {
                currentWord.append(ch);
            }

            if ((currentWord.length() > 2) && (dictionary.contains(currentWord.toString()))) {
                validWords.add(currentWord.toString());
            }

            if (((Queue<String>) dictionary.keysWithPrefix(currentWord.toString())).size() != 0) {
                for (int w : adjacent(v)) {
                    if (!marked[w]) {
                        runDfs(w);
                    }
                }
            }

            currentWord.deleteCharAt(currentWord.length() - 1);
            marked[v] = false;
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
            } else if (f == (numOfCols - 1) * numOfRows) {
                adj = new int[]{f - numOfCols,
                        f - numOfCols + 1,
                        f + 1};
            } else if (((numOfCols - 1) * numOfRows < f) && (f < numOfCols * numOfRows - 1)) {
                adj = new int[]{f - numOfCols - 1,
                        f - numOfCols,
                        f - numOfCols + 1,
                        f - 1,
                        f + 1};
            } else if (f == numOfCols * numOfRows - 1) {
                adj = new int[]{f - numOfCols - 1,
                        f - numOfCols,
                        f - 1};
            } else if (f % numOfCols == 0) {
                adj = new int[]{f - numOfCols,
                        f - numOfCols + 1,
                        f + 1,
                        f + numOfCols,
                        f + numOfCols + 1};
            } else if ((f % numOfCols != 0) && (f % numOfCols != numOfCols - 1)) {
                adj = new int[]{f - numOfCols - 1,
                        f - numOfCols,
                        f - numOfCols + 1,
                        f - 1,
                        f + 1,
                        f + numOfCols - 1,
                        f + numOfCols,
                        f + numOfCols + 1};
            } else {
                adj = new int[]{f - numOfCols - 1,
                        f - numOfCols,
                        f - 1,
                        f + numOfCols - 1,
                        f + numOfCols};
            }

            return adj;
        }
    }
}
