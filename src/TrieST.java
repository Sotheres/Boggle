import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class TrieST {

    private static final int R = 26;

    public Node root;

    public static class Node {

        public boolean notNull = false;
        public Integer val;
        public final Node[] next = new Node[R];
    }

    public Integer get(String key) {
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

        return x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    public void put(String key, Integer val) {
        if (key == null || val == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }

        if (root == null) {
            root = new Node();
        }
        Node x = root;
        int d = 0;

        while (d != key.length()) {
            char c = key.charAt(d);
            if (x.next[c - 65] == null) {
                x.next[c - 65] = new Node();
                x.notNull = true;
            }
            x = x.next[c - 65];
            d++;
        }

        x.val = val;
    }

    public boolean isKeysWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        Node x = getNodeOf(prefix);
        if (x == null) {
            return false;
        }
        collect(x, new StringBuilder(prefix), results);
        return results.size() != 0;
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
        Stack<StringBuilder> strStack = new Stack<>();
        Stack<Node> nodeStack = new Stack<>();

        strStack.push(prefix);
        nodeStack.push(x);

        while (!strStack.isEmpty()) {
            prefix = strStack.pop();
            x = nodeStack.pop();

            if (x.val != null) {
                results.enqueue(prefix.toString());
            }

            for (char c = 65; c < 65 + R; c++) {
                prefix.append(c);
                if (x.next[c - 65] != null) {
                    strStack.push(prefix);
                    nodeStack.push(x.next[c - 65]);
                }
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }
}