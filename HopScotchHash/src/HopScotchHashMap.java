import java.util.*;

/**
 * Hop Scotch Hash Map Implementation
 * @author Zack Sparks
 * @version 1.0
 */
public class HopScotchHashMap<K extends Comparable, V> implements Map<K, V> {

    private int count;
    private final int NEIGHSIZE = 4;
    private final double LF = 0.9;
    private final int DEFAULT_SIZE = 11;
    private Bucket<K, V>[] spine;

    // MAD Hashing Function Parameters
    private int a1;
    private int b1;
    private int p;

    final private static TreeSet<Integer> optimusPrimes;
    final private static Random r;

    // Static block to initialize the prime tree and random object
    static {
        Integer[] primes = {11, 23, 53, 97, 193, 389, 769, 1543, 3079, 6151,
                12289, 24593, 49157, 98317, 196613, 400009, 800011, 16000057};
        optimusPrimes = new TreeSet<>();
        Collections.addAll(optimusPrimes, primes);

        r = new Random(123);
    }

    /**
     * Constructs a new HopScotchHAshMap of default size
     */
    public HopScotchHashMap() {
        spine = new Bucket[DEFAULT_SIZE];
        count = 0;

        redrawMAD();
    }


    /**
     * @return the size of the Map
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * @return Whether the map is empty or not
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Checks if the map has the passed key
     * @param key the key to search for
     * @return whether or not the key is contained in the map
     */
    @Override
    public boolean containsKey(Object key) {
        K searchKey = (K) key;

        if (getKey(searchKey) != -1) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the map has the passed value
     * @param value the value to search for
     * @return Whether or not the map has the value
     */
    @Override
    public boolean containsValue(Object value) {
        V searchVal = (V) value;

        for (Bucket b : spine) {
            if (!(b == null) && b.getValue().equals(searchVal)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the value from the passed key if the map contains the key
     * @param key The key of the key, value pair in which to get the value of
     * @return the value of the key, value pair in the map or null if the key
     *         is not in the map
     */
    @Override
    public V get(Object key) {
        K searchKey = (K) key;
        int searchPos = hash(searchKey) % spine.length;
        int i = searchPos;

        while (true) {
            if (spine[i] == null || i == searchPos + NEIGHSIZE) {
                break;
            } else {
                if (spine[i].getKey().equals(searchKey)) {
                    return (spine[i].isDeleted()) ? null : spine[i].getValue();
                }

                i++;
            }
        }

        return null;
    }

    /**
     * Puts the key value pair into the map
     * @param key the key of the key, value pair
     * @param value the value of the key, value pair
     * @return If the key was already in the map before, it will return the
     *         old value.
     */
    @Override
    public V put(K key, V value) {
        // will regrow if the LF is exceeded
        if ((double) count / (double) spine.length > LF) {
            regrow();
        }

        int pos = hash(key) % spine.length;
        Bucket<K, V> newBucket = new Bucket<>(key, value, pos);
        V result = null;

        // If the position if null, deleted or equal just put it there
        if (spine[pos] == null || spine[pos].isDeleted() ||
                spine[pos].getKey().equals(key)) {

            boolean increment = true;

            if (spine[pos] != null && !spine[pos].isDeleted()) {
                increment = !spine[pos].getKey().equals(key);
            }

            result = getOldValue(pos);
            spine[pos] = newBucket;

            if (increment) {
                count++;
            }
        } else { // The position is filled, look for open position
            int firstOpen = findOpen(pos, key);

            while (firstOpen == Integer.MIN_VALUE) { // Regrow happened!
                firstOpen = findOpen(pos - 1, key); // start at hash position
            }

            if (firstOpen < 0) { // key already exists
                result = spine[-1 * firstOpen].getValue();
                spine[-1 * firstOpen] = newBucket;
            } else if (firstOpen < pos + NEIGHSIZE) { // empty is in the hood
                spine[firstOpen] = newBucket;
                count++;
            } else { // Empty is outside the neighborhood, need to evict!
                int newPos = openBucketInNeigh(pos, firstOpen);

                if (newPos == Integer.MIN_VALUE) { // Had to regrow
                    return put(key, value);
                }

                spine[newPos] = newBucket;
                count++;
            }
        }

        return result;
    }

    /**
     * Remove the key, value pair that matches the passed key
     * @param key the key to search for in the map
     * @return the value of the key that was removed or null if the key wasn't
     *         in the map
     */
    @Override
     public V remove(Object key) {
        K searchKey = (K) key;

        int pos = getKey(searchKey);

        if (pos >= 0) {
            spine[pos].setDeleted(true);
            count--;
            return spine[pos].getValue();
        }
        return null;
    }

    /**
     * Puts all the entries from the passed map into this map
     * @param m the map to copy entries from
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

        for (Map.Entry e : m.entrySet()) {
            put((K) e.getKey(), (V) e.getValue());
        }

    }

    /**
     * Deletes the map and starts over
     */
    @Override
    public void clear() {
        spine = new Bucket[DEFAULT_SIZE];
        count = 0;
    }

    /**
     * @return a set containing all the keys in the map
     */
    @Override
    public Set keySet() {
        HashSet<K> result = new HashSet<>();

        for (Bucket<K, V> b : spine) {
            if (!isNullOrDeleted(b)) {
                result.add(b.getKey());
            }
        }

        return result;
    }

    /**
     * @return A collection of the values contained in the map
     */
    @Override
    public Collection values() {
        ArrayList<V> result = new ArrayList<>();

        for (Bucket<K, V> b : spine) {
            if (!isNullOrDeleted(b)) {
                result.add(b.getValue());
            }
        }

        return result;
    }

    /**
     * @return returns a set of all entries in the map
     */
    @Override
    public Set<Entry<K, V>> entrySet() {

        TreeSet<Entry<K, V>> result = new TreeSet<>();

        for (Bucket<K, V> b : spine) {
            if (!isNullOrDeleted(b)) {
                result.add(b);
            }
        }

        return result;
    }

    /**
     * Regrows the HopScotchHashMap to next optimal prime number.  Also, it
     * resamples the MAD parameters to change the hash function.
     */
    private void regrow() {
        Bucket<K, V>[] old = spine;

        spine = new Bucket[p];

        count = 0;

        redrawMAD();

        for (int i = 0; i < old.length; i++) {
            if (!isNullOrDeleted(old[i])) {
                put(old[i].getKey(), old[i].getValue());
            }
        }
    }

    /**
     * Saves a few character inputs
     * @param b bucket to check if is null or deleted
     * @return whether or not the bucket is null or deleted
     */
    private boolean isNullOrDeleted(Bucket b) {
        return (b == null || b.isDeleted());
    }


    /**
     * @param searchKey the key to get the position of in the backing array
     * @return the position of the key searched for, -1 if not found
     */
    private int getKey(K searchKey) {
        int searchPos = hash(searchKey) % spine.length;
        int i = searchPos;

        while (true) {
            if (spine[i] == null || i == searchPos + NEIGHSIZE) {
                return -1;
            } else {
                if (spine[i].getKey().equals(searchKey)) {
                    return i;
                }

                i++;
            }
        }
    }

    /**
     * @param i The index to look in
     * @return the value at that position or null if the bucket is null
     */
    private V getOldValue(int i) {
        return (spine[i] == null) ? null : spine[i].getValue();
    }

    /**
     * Will return an open position to put new value into.  If necessary will
     * hop scotch to open up space in the neighborhood and regrow if necessary
     * @param neighStart The neighborhood th new value needs to be in
     * @param curOpenSlot the current open slot in the backing array
     * @return the position to insert the new value in. Or Integer.MIN_VALUE if
     *         the map was regrown
     */
    private int openBucketInNeigh(int neighStart, int curOpenSlot) {
        boolean flag = true;
        int candidatePos = curOpenSlot - NEIGHSIZE + 1;
        Bucket<K, V> candidate = spine[candidatePos];

        while (flag) {
            if (candidate == null) { // Made it back to the open slot. regrow!
                regrow();

                return Integer.MIN_VALUE;
            } else if (curOpenSlot - candidate.getBase() < NEIGHSIZE) { // G2G
                spine[curOpenSlot] = candidate;
                spine[candidatePos] = null;
                curOpenSlot = candidatePos;
                flag = false;
            } else { // Not a valid candidate to switch with open, continue on..
                candidatePos++;
                candidate = spine[candidatePos];
            }
        }

        /*
            This bit will check if the new key is ok to put in the newly opened
            spot.  If not, it will hopscotch the open slot again...
         */
        if (curOpenSlot - neighStart > NEIGHSIZE) {
            return openBucketInNeigh(neighStart, curOpenSlot);
        } else {
            return curOpenSlot;
        }
    }

    /**
     * Finds the first open position in the bucket array for insertion or the
     * position where the key already exists
     * @param i the starting position to search from
     * @return the first empty slot in the backing array of buckets or a
     *         negative int if the slot is occupied by the same key
     */
    private int findOpen(int i, K k) {
        boolean flag = true;

        while(flag) {
            i++;

            if (i >= spine.length) {
                regrow();
                return Integer.MIN_VALUE;
            } else if (spine[i] == null) {
                flag = false;
            } else if (spine[i].getKey().equals(k)) {
                i = i * -1;
                flag = false;
            } else if (spine[i].isDeleted()) {
                flag = false;
            }
        }

        return i;
    }

    /**
     * @param k the key to hash
     * @return the MAD method hash for the corresponding key.
     */
    private int hash(K k) {
        return (Math.abs((a1 * Math.abs(k.hashCode() + b1)) % p) %
                spine.length);
    }

    /**
     * Resamples from Random the MAD parameters
     */
    private void redrawMAD() {
        p = optimusPrimes.ceiling(spine.length + 1);
        a1 = r.nextInt(p - 1) + 1; // Has to be positive
        b1 = r.nextInt(p);
    }

    /**
     * The Bucket class for the HopScotchHashMap
     * @param <K> Key
     * @param <V> Value
     */
    private class Bucket<K extends Comparable, V> implements Map.Entry<K,V>,
            Comparable<Bucket<K, V>> {
        final private K key;
        private V value;
        private boolean deleted;
        private int base;

        /**
         * Constructs a new bucket
         * @param k the key for the bucket
         * @param v the value for the bucket
         * @param b the original hashed position (base) of the bucket
         */
        public Bucket(K k, V v, int b) {
            key = k;
            value = v;
            base = b;
            deleted = false;
        }

        /**
         * CompareTo implementation
         * @param b bucket to compare to this bucket, on the basis of the keys
         * @return the value 0 if this Bucket is equal to the argument Bucket;
         * a value less than 0 if this Bucket is numerically less than the
         * argument Bucket; and a value greater than 0 if this Bucket is
         * numerically greater than the argument Bucket. Wording from Java API.
         */
        @Override
        public int compareTo(Bucket<K, V> b) {
            K k = b.getKey();
            return key.compareTo(k);
        }

        /**
         * @return the Key of this bucket
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * @return the value of this bucket
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * @param v the value to be set for this bucket
         * @return the old value of this bucket
         */
        @Override
        public V setValue(V v) {
            V oldV = value;
            value = v;
            return oldV;
        }

        /**
         * Sets the base (the original hashed position) of this bucket
         * @param i the original hashed position
         */
        public void setBase(int i) {
            base = i;
        }

        /**
         * @return the base of the bucket
         */
        public int getBase() {
            return base;
        }

        /**
         * @param isDeleted what the bucket's deleted state is to be set to
         */
        public void setDeleted(boolean isDeleted) {
            deleted = isDeleted;
        }

        /**
         * @return Whether or not this bucket is deleted.
         */
        public boolean isDeleted() {
            return deleted;
        }
    }
}
