import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final TrieST dictionary;
    private Set<String> validWords;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TrieST();
        for (String s : dictionary) {
            this.dictionary.put(s, 1);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new HashSet<>();
        int rows = board.rows();
        int cols = board.cols();

        for (int i = 0; i < rows * cols; i++) {
            new DFS(rows, cols, board).run(i, dictionary.getRoot());
        }

        return validWords;
    }

    public int scoreOf(String word) {
        if (!dictionary.contains(word)) {
            return 0;
        }
        if (word.length() < 3) {
            return 0;
        } else if (word.length() <= 4) {
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
    }

    private class DFS {

        private final boolean[] marked;
        private final int numOfRows;
        private final int numOfCols;
        private final StringBuilder currentWord;
        private final char[] letters;

        public DFS(int rows, int cols, BoggleBoard board) {
            this.numOfRows = rows;
            this.numOfCols = cols;
            marked = new boolean[rows * cols];
            letters = new char[rows * cols];
            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfCols; j++) {
                    letters[i * numOfCols + j] = board.getLetter(i, j);
                }
            }
            currentWord = new StringBuilder();
        }

        public void run(int v, TrieST.Node prev) {
            char ch = letters[v];
            TrieST.Node curr = prev.getNext(ch - 65);

            if (ch == 'Q') {
                currentWord.append("QU");
                if (curr != null) {
                    curr = curr.getNext('U' - 65);
                }
            } else {
                currentWord.append(ch);
            }

            marked[v] = true;

            if ((currentWord.length() > 2) && (curr != null && curr.getVal() != 0)) {
                validWords.add(currentWord.toString());
            }

            if (curr != null && curr.isNotNull()) {
                for (int w : adjacent(v)) {
                    if (!marked[w]) {
                        run(w, curr);
                    }
                }
            }

            if (ch == 'Q') {
                currentWord.delete(currentWord.length() - 2, currentWord.length());
            } else {
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
            marked[v] = false;
        }

        private int[] adjacent(int f) {
            if (numOfRows == 1 && numOfCols == 1) {
                return new int[0];
            } else if (numOfRows == 1 || numOfCols == 1) {
                return exoticAdj(f);
            }

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
            } else if (f == (numOfRows - 1) * numOfCols) {
                adj = new int[]{f - numOfCols,
                        f - numOfCols + 1,
                        f + 1};
            } else if (((numOfRows - 1) * numOfCols < f) && (f < numOfCols * numOfRows - 1)) {
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

        private int[] exoticAdj(int f) {
            int[] adj;

            if (f == 0) {
                adj = new int[]{1};
            } else if (((numOfCols == 1) && (f != numOfRows - 1))
                    || ((numOfRows == 1) && (f != numOfCols - 1))) {
                adj = new int[]{f - 1, f + 1};
            } else {
                adj = new int[]{f - 1};
            }

            return adj;
        }
    }
}
