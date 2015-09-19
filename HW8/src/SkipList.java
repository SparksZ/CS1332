import java.util.*;

/**
 * Implements Skip Lists
 * @param <K> the keys of the Skip List must extend Integer
 * @param <V> the values of the key, value pairs
 */
public class SkipList<K extends Integer, V> implements java.util.Map<K, V> {
    private Node<K, V> head;
    private Node<K, V> tail;
    int count;
    private Random r = new Random(1);

    /**
     * Constructs a new empty skip list
     */
    public SkipList() {
        head = new Node(Integer.MIN_VALUE, null);
        tail = new Node(Integer.MAX_VALUE, null);

        head.next = tail;
        tail.prev = head;
        count = 0;
    }

    /**
     * @return the size of the skip list (number of entries)
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * @return whether or not it is empty
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * @param k the key to search for
     * @return Whether or not the key is contained in the skip list
     */
    @Override
    public boolean containsKey(Object k) {
        K key = (K) k;

        Node result = getNodeOrPrevious(key);

        if (result.getKey().equals(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param v the value to search for
     * @return whether or not the value is in the list
     */
    @Override
    public boolean containsValue(Object v) {
        V value = (V) v;

        Node node = getBottomSentinel().next;

        while (node.getKey() < Integer.MAX_VALUE) {
            if (node.getValue().equals(value)) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    /**
     * @param k the key of the key, value pair to search for
     * @return the value associated with the key or null if key not found
     */
    @Override
    public V get(Object k) {
        K key = (K) k;
        Node result = getNodeOrPrevious(key);

        if (result.getKey().equals(key)) {
            return (V) result.getValue();
        } else {
            return null;
        }
    }

    /**
     * Inserts the key, value pair into the skip list
     * @param key the key of the key, value pair
     * @param value the value of the key, value pair
     * @return The old value that was replaced if the key already existed, or
     *         null if the key did not exist before.
     */
    @Override
    public V put(K key, V value) {
        Node node = getNodeOrPrevious(key);

        if (node.getKey().equals(key)) { // Key exists change value
            V oldV = (V) node.getValue();
            node.setValue(value);

            while (node.down != null) {
                node = node.down;
                node.setValue(value);
            }

            return oldV;
        } else { // Key doesn't exist: create and start flipping!
            Node newNode = new Node(key, value);
            Node afterNode = node.next;

            node.next = newNode;
            afterNode.prev = newNode;

            newNode.prev = node;
            newNode.next = afterNode;

            while (r.nextBoolean()) {
                Node upNewNode = new Node(key, value);
                Node upPrev = getPrevUpOneLevel(newNode);
                afterNode = upPrev.next;

                upNewNode.down = newNode;
                upNewNode.next = afterNode;

                upPrev.next = upNewNode;
                afterNode.prev = upNewNode;

                upNewNode.next = afterNode;
                upNewNode.prev = upPrev;
                newNode.up = upNewNode;

                newNode = upNewNode;
            }

            count++;
        }

        return null; // No Previous value
    }

    /**
     * Removes the key, value pair associated with the given key
     * @param k the key to search for and remove
     * @return the value of the key, value pair that was removed
     */
    @Override
    public V remove(Object k) {
        K key = (K) k;
        Node node = getNodeOrPrevious(key);

        V oldV;

        if (!node.getKey().equals(key)) {
            return null; // Wasn't in the list
        } else {
            oldV = (V) node.getValue();
            boolean flag = true;
            while (flag) {
                Node prev = node.prev;
                Node next = node.next;

                prev.next = next;
                next.prev = prev;

                if (node.down != null) {
                    node = node.down;
                } else {
                    flag = false;
                }
            }
        }

        count--;
        return oldV;
    }

    /**
     * Puts each of the entries from the passed Map into the skip list
     * @param m the map which contains the entries to be added to the list
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * Clears the skip list and starts over
     */
    @Override
    public void clear() {
        head = new Node(Integer.MIN_VALUE, null);
        tail = new Node(Integer.MAX_VALUE, null);

        head.next = tail;
        tail.prev = head;
        count = 0;
    }

    /**
     * @return a set of the keys in the list
     */
    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();

        Node node = getBottomSentinel().next;

        while (node.getKey() < Integer.MAX_VALUE) {
            result.add((K) node.getKey());
            node = node.next;
        }

        return result;
    }

    /**
     * @return a collection of the values in the list
     */
    @Override
    public Collection<V> values() {
        Collection<V> result = new ArrayList<>();

        Node node = getBottomSentinel().next;

        while (node.getKey() < Integer.MAX_VALUE) {
            result.add((V) node.getValue());
            node = node.next;
        }

        return result;
    }

    /**
     * @return a set of all the key value pairs in the list
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<>();

        Node node = getBottomSentinel().next;

        while (node.getKey() < Integer.MAX_VALUE) {
            Entry temp =
                    new AbstractMap.SimpleEntry<>(node.getKey(),
                            node.getValue());

            result.add(temp);
            node = node.next;
        }

        return result;
    }

    /**
     * @param key the key we are looking for in the Skip List
     * @return either the node with the key equal to the passed key or
     *         the node to the immediate left of where the new node should go
     */
    private Node getNodeOrPrevious(K key) {
        Node<K, V> node = head;
        Boolean flag = true;

        while (flag) {
            if (node.getKey().equals(key)) {
                return node;
            } else if (node.compareNextKey(key) <= 0) {
                node = node.next;
            } else if (node.compareNextKey(key) > 0 &&
                    node.down != null) {
                node = node.down;
            } else {
                flag = false;
            }
        }

        return node; // The previous node
    }

    /**
     * @return the bottom sentinel node
     */
    private Node getBottomSentinel() {
        Node node = head;

        while (node.down != null) {
            node = node.down;
        }

        return node;
    }

    /**
     * Searches the list for the first previous up to the next level in the list
     * if it gets to the sentinel and there is no list above, it will create one
     * @param node the node to start searching from (immediately look prev)
     * @return the node to the immediate left of where the new node should go
     */
    private Node getPrevUpOneLevel(Node node) {

        node = node.prev;

        while (node.up == null) {
            if (node.key.equals(Integer.MIN_VALUE)) {
                newLayer();
                return head;
            } else {
                node = node.prev;
            }
        }

        return node.up;
    }

    /**
     * Creates a new list layer above the head node
     */
    private void newLayer() {
        Node newHead = new Node(Integer.MIN_VALUE, null);
        Node newTail = new Node(Integer.MAX_VALUE, null);

        head.up = newHead;
        tail.up = newTail;

        newHead.next = newTail;
        newHead.down = head;
        newTail.prev = newHead;
        newTail.down = tail;

        head = newHead;
        tail = newTail;
    }

    /**
     * The node for the skip lists
     * @param <K> the key of the key, value pair
     * @param <V> the value of the key, value pair
     */
    private class Node<K extends Integer, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        public Node<K, V> next;
        public Node<K, V> prev;
        public Node<K, V> up;
        public Node<K, V> down;

        /**
         * Constructs a new node
         * @param k the key of the key, value pair
         * @param v the value of the key, value pair
         */
        public Node(K k, V v) {
            key = k;
            value = v;
            this.next = null;
            this.prev = null;
            this.up = null;
            this.down = null;

        }

        /**
         * @param v the new value of the key, value pair
         * @return the old value before the new value was set
         */
        public V setValue(V v) {
            V oldV = value;
            value = v;
            return oldV;
        }

        /**
         * @return the key of the node
         */
        public K getKey() {
            return key;
        }

        /**
         * @return the value of the node
         */
        public V getValue() {
            return value;
        }

        /**
         * Compares the passed key to the next nodes key
         * @param key the key to compare the next key to
         * @return the comparison of the passed key and the next node's key
         */
        public int compareNextKey(K key) {
            return this.next.getKey().compareTo(key);
        }
    }
}
