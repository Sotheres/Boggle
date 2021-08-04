public class TrieST {

    private static final int R = 26;

    private Node root;

    public static class Node {

        private boolean notNull = false;
        private int val;
        private final Node[] next = new Node[R];

        public boolean isNotNull() {
            return notNull;
        }

        public int getVal() {
            return val;
        }

        public Node getNext(int i) {
            return next[i];
        }
    }

    public Node getRoot() {
        return root;
    }

    public int get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (root == null) {
            return 0;
        }

        Node x = root;
        int d = 0;
        while (d != key.length()) {
            char c = key.charAt(d);
            x = x.next[c - 65];
            if (x == null) {
                return 0;
            }
            d++;
        }

        return x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != 0;
    }

    public void put(String key, int val) {
        if (key == null) {
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
}