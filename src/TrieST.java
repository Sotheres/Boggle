import edu.princeton.cs.algs4.Queue;

public class TrieST<Value> {
    private static final int R = 26;


    private Node root;      // root of trie
    private int n;          // number of keys in trie

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (root == null) {
            return null;
        }

        Node x = root;
        int d = 0;
        while (d != key.length()) {
            char c = key.charAt(d);
            x = x.next[c - 65];
            if (x == null) {
                return null;
            }
            d++;
        }

        return (Value) x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            delete(key);
        } else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            if (x.val == null) {
                n++;
            }
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 65] = put(x.next[c - 65], key, val, d + 1);
        return x;
    }


    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        Node x = getNodeOf(prefix);
        if (x == null) {
            return results;
        }
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private Node getNodeOf(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (root == null) {
            return null;
        }

        Node x = root;
        int d = 0;
        while (d != key.length()) {
            char c = key.charAt(d);
            x = x.next[c - 65];
            if (x == null) {
                return null;
            }
            d++;
        }

        return x;
    }

    private void collect(Node init, StringBuilder prefix, Queue<String> results) {
        Node x = init;
        Queue<StringBuilder> qCh = new Queue<>();
        Queue<Node> qNode = new Queue<>();

        for (char c = 65; c < 65 + R; c++) {
            prefix.append(c);
            if (x.next[c - 65] != null) {
                qCh.enqueue(prefix);
                qNode.enqueue(x.next[c - 65]);
            }
            prefix.deleteCharAt(prefix.length() - 1);
        }

        while(!qCh.isEmpty()) {
            prefix = qCh.dequeue();
            x = qNode.dequeue();

            if (x.val != null) {
                results.enqueue(prefix.toString());
            }

            for (char c = 65; c < 65 + R; c++) {
                prefix.append(c);
                if (x.next[c - 65] != null) {
                    qCh.enqueue(prefix);
                    qNode.enqueue(x.next[c - 65]);
                }
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }

    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     *
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     * as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.val != null)
            results.enqueue(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     *
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d + 1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) n--;
            x.val = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }
}
