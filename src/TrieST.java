import edu.princeton.cs.algs4.Queue;

public class TrieST {

    private static final int R = 26;
    private Node root = new Node();

    private static class Node {
        private Integer val;
        private Node[] next = new Node[R];
    }

    public void put(String key, Integer val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Integer val, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Integer get(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    public boolean isPrefix(String prefix) {
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, prefix, queue);
        return queue.size() != 0;
    }

    private void collect(Node x, String prefix, Queue<String> q) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            q.enqueue(prefix);
        }
        for (char c = 0; c < R; c++) {
            collect(x.next[c], prefix + c, q);
        }
    }
}