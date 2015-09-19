import java.util.*;

/**
 * CuckooMap class representing implementation of Cuckoo Hashing
 * @author Zack Sparks
 * @version 1.0
 * @param <K> Generic type for keys
 * @param <V> Generic type for values
 */
public class CuckooMap<K extends Hashable, V> implements Map<K,V> {

    private int count1;
    private int count2;

    // MAD Hashing Function Parameters
    private int a1;
    private int b1;
    private int a2;
    private int b2;
    private int p;

    final private static TreeSet<Integer> optimusPrimes;
    final private static Random r;

    // Static block to initialize the prime tree and random object
    static {
        Integer[] primes = {11, 23, 53, 97, 193, 389, 769, 1543, 3079, 6151,
                12289, 24593, 49157, 98317, 196613, 400009, 800011, 16000057};
        optimusPrimes = new TreeSet<>();
        Collections.addAll(optimusPrimes, primes);

        r = new Random();
    }

    private Bucket<K, V>[] buckets1;
    private Bucket<K, V>[] buckets2;

    final private int DEFAULT_SIZE = 11;
    final private double LOAD_FACTOR = 0.5;
    final private int MAX_CALL_BEFORE_REGROW = 15;

    /**
     * Constructs a new CuckooMap
     */
    public CuckooMap() {
        buckets1 = new Bucket[DEFAULT_SIZE];
        buckets2 = new Bucket[DEFAULT_SIZE];

        count1 = 0;
        count2 = 0;

        redrawMAD();
    }

    /**
     * @return the size of the CuckooMap
     */
    @Override
    public int size() {
        return count1 + count2;
    }

    /**
     * @return whether or not the CuckooMap is empty
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @param key the key to check the CuckooMap for
     * @return whether or not the CuckooMap contains the passed key
     */
    @Override
    public boolean containsKey(Object key) {
        K keyTest = (K) key;

        Bucket<K, V> result = getBucket(keyTest);

        return !(isNullOrDeleted(result));
    }

    /**
     * @param value The value to look for in the CuckooMap
     * @return Whether or not the value is contained in the CuckooMap
     */
    @Override
    public boolean containsValue(Object value) {
        for (Bucket b : buckets1) {
            if (!isNullOrDeleted(b) && value.equals(b.getValue())) {
                return true;
            }
        }

        for (Bucket b : buckets2) {
            if (!isNullOrDeleted(b) && value.equals(b.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param key The key look for in the CuckooMap
     * @return The value of the key, value pair null if key isn't found
     */
    @Override
    public V get(Object key) {
        K keyTest = (K) key;

        Bucket<K, V> returnBucket = getBucket(keyTest);

        return (isNullOrDeleted(returnBucket)) ? null : returnBucket.getValue();
    }

    /**
     * @param key The key in the Key, Value pair to insert into the CuckooMap
     * @param value The Value in the Key,
     *              Value pair to insert into the CuckooMap
     * @return The Value replaced if the Key was already in the CuckooMap
     */
    @Override
    public V put(K key, V value) {
        if ((double) (count1 + 1) / (double) buckets1.length > LOAD_FACTOR ||
                (double) (count2 + 1) / (double) buckets2.length > LOAD_FACTOR) {
            regrow();
        }

        Bucket<K, V> newBucket = new Bucket<>(key, value);

        int index1 = hash(key, 1);
        int index2 = hash(key, 2);

        // Makes sure that the key isn't already in either table
        if (!isNullOrDeleted(buckets1[index1]) && buckets1[index1].getKey().
                equals(key)) {
            return putBucket(newBucket, 1, 0);
        } else if (!isNullOrDeleted(buckets2[index2]) && buckets2[index2].
                getKey().equals(key)) {
            return putBucket(newBucket, 2, 0);
        }

        return putBucket(newBucket, 1, 0);
    }

    /**
     * Recursive function to put buckets into the CuckooMap
     * @param b The bucket to put into the Map
     * @param tableToTry This is the table to attempt to add the bucket to
     *                   [1, 2]
     * @param callNum This keeps track of the number of calls on the stack, if
     *                the calls go above MAX_CALL_BEFORE_REGROW the table will
     *                regrow and try again.
     * @return The previous value if the key is already in the table
     */
    private V putBucket(Bucket<K, V> b, int tableToTry, int callNum) {

        if (callNum > MAX_CALL_BEFORE_REGROW) {
            regrow();
            callNum = 0;
            tableToTry = 1;
        }

        int index = hash(b.getKey(), tableToTry);
        Bucket<K, V>[] table = (tableToTry == 1) ? buckets1 :
                buckets2;

        Bucket<K, V> temp = table[index];

        if (isNullOrDeleted(temp)) {
            table[index] = b;

            if (tableToTry == 1) {
                count1++;
            } else {
                count2++;
            }

            return null;
        } else if (temp.getKey().equals(b.getKey())) {
            return temp.setValue(b.getValue());
        } else {
            Bucket<K, V> kickedOut = temp;
            table[index] = b;
            return putBucket(kickedOut, 2 - ((tableToTry + 1) % 2), ++callNum);
        }
    }

    /**
     * @param key the key of the Key, Value pair to be removed
     * @return the Value of the removed Key, Value pair, null if not found
     */
    @Override
    public V remove(Object key) {
        K keyTest = (K) key;

        Bucket<K, V> result = getBucket(keyTest);

        if (result != null && !result.isDeleted()) {
            result.setDeleted(true);
            return result.getValue();
        }

        return null;
    }

    /**
     * Puts all elements from the passed Map into the CuckooMap
     * @param m Map whose elements are to be added to CuckooMap
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Map<K, V> mapAdd = (Map<K, V>) m;

        for (Entry<K, V> putMe : mapAdd.entrySet()) {
            put(putMe.getKey(), putMe.getValue());
        }
    }

    /**
     * Clears the CuckooMap and starts over
     */
    @Override
    public void clear() {
        buckets1 = new Bucket[DEFAULT_SIZE];
        buckets2 = new Bucket[DEFAULT_SIZE];

        count1 = 0;
        count2 = 0;
    }

    /**
     * @return A Set of all the keys in the CuckooMap
     */
    @Override
    public Set<K> keySet() {
        TreeSet<K> result = new TreeSet<>();

        for (Bucket<K, V> b : buckets1) {
            if (b != null && !b.isDeleted()) {
                result.add(b.getKey());
            }
        }

        for (Bucket<K, V> b : buckets2) {
            if (b != null && !b.isDeleted()) {
                result.add(b.getKey());
            }
        }

        return result;
    }

    /**
     * @return A collection of all the values in the CuckooMap
     */
    @Override
    public Collection<V> values() {
        ArrayList<V> result = new ArrayList<>();

        for (Bucket<K, V> b : buckets1) {
            if (b != null && !b.isDeleted()) {
                result.add(b.getValue());
            }
        }

        for (Bucket<K, V> b : buckets2) {
            if (b != null && !b.isDeleted()) {
                result.add(b.getValue());
            }
        }

        return result;
    }

    /**
     * @return A set of the Key, Value pairs in the form of Map.Entry
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        TreeSet<Entry<K, V>> result = new TreeSet<>();

        for (Bucket<K, V> b : buckets1) {
            if (!isNullOrDeleted(b)) {
                result.add(b);
            }
        }

        for (Bucket<K, V> b : buckets2) {
            if (!isNullOrDeleted(b)) {
                result.add(b);
            }
        }

        return result;
    }

    /**
     * Regrows the CuckooMap to size next optimal prime number. Also, it
     * resamples the MAD parameters to change the hash functions
     */
    private void regrow() {
        Bucket<K, V>[] old1 = buckets1;
        Bucket<K, V>[] old2 = buckets2;

        buckets1 = new Bucket[p]; // MAD conveniently uses the next largest prime number
        buckets2 = new Bucket[p];
        count1 = 0;
        count2 = 0;

        redrawMAD();

        for (int i = 0; i < old1.length; ++i) {
            if (!isNullOrDeleted(old1[i])) {
                put(old1[i].getKey(), old1[i].getValue());
            }

            if (!isNullOrDeleted(old2[i])) {
                put(old2[i].getKey(), old2[i].getValue());
            }
        }
    }

    /**
     * @param k the key to search the CuckooMap for
     * @return Reference to the bucket found, null if not found.
     */
    private Bucket getBucket(K k) {

        Bucket<K, V> bucket1 = buckets1[hash(k, 1)];

        if (!isNullOrDeleted(bucket1) && bucket1.getKey().equals(k)) {
            return bucket1;
        }

        Bucket<K, V> bucket2 = buckets2[hash(k, 2)];

        if (!isNullOrDeleted(bucket2) && bucket2.getKey().equals(k)) {
            return bucket2;
        }

        return null;
    }

    /**
     * @param k the key to hash
     * @param i the corresponding table to indicate which hash to use
     * @return the MAD method hash for the corresponding table and key.
     */
    private int hash(K k, int i) {
        return (i == 1) ? (Math.abs((a1 * Math.abs(k.hash1() + b1)) % p) %
                buckets1.length) : (Math.abs((a2 * Math.abs(k.hash2() + b2))
                % p) % buckets2.length);
    }

    /**
     * Resamples from Random the MAD parameters
     */
    private void redrawMAD() {
        p = optimusPrimes.ceiling(buckets1.length + 1);
        a1 = r.nextInt(p - 1) + 1; // Has to be positive
        b1 = r.nextInt(p);
        a2 = r.nextInt(p - 1) + 1;
        b2 = r.nextInt(p);
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
     * The Bucket class for the CuckooMap
     * @param <K> Key
     * @param <V> Value
     */
    private class Bucket<K extends Comparable, V> implements Map.Entry<K,V>,
            Comparable<Bucket<K, V>> {
        final private K key;
        private V value;
        private boolean deleted;

        /**
         * Constructs a new bucket
         * @param k the key for the bucket
         * @param v the value for the bucket
         */
        public Bucket(K k, V v) {
            key = k;
            value = v;
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
