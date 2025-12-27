/**
 * Simple hash map using separate chaining for collision resolution.
 * Used for fast airport lookup by IATA code.
 * 
 * Source: AI-generated based on standard HashMap implementation with chaining
 */
public class SimpleHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    private DynamicList<Entry<K, V>>[] buckets;
    private int size;
    private int capacity;
    
    @SuppressWarnings("unchecked")
    public SimpleHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new DynamicList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new DynamicList<>();
        }
        this.size = 0;
    }
    
    /**
     * Put key-value pair into map.
     * Time: O(1) average, O(n) worst case (many collisions in one bucket)
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int bucketIndex = getBucketIndex(key);
        DynamicList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        // Check if key already exists, update value
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        
        // Add new entry
        bucket.add(new Entry<>(key, value));
        size++;
        
        // Rehash if load factor exceeded
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }
    
    /**
     * Get value by key.
     * Time: O(1) average, O(n) worst case
     */
    public V get(K key) {
        if (key == null) return null;
        
        int bucketIndex = getBucketIndex(key);
        DynamicList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }
    
    /**
     * Check if key exists.
     * Time: O(1) average
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    /**
     * Remove key-value pair.
     * Time: O(1) average
     */
    public V remove(K key) {
        if (key == null) return null;
        
        int bucketIndex = getBucketIndex(key);
        DynamicList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                bucket.remove(i);
                size--;
                return entry.value;
            }
        }
        return null;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Get all values in the map.
     * Time: O(n)
     */
    public DynamicList<V> values() {
        DynamicList<V> allValues = new DynamicList<>();
        for (int i = 0; i < capacity; i++) {
            DynamicList<Entry<K, V>> bucket = buckets[i];
            for (int j = 0; j < bucket.size(); j++) {
                allValues.add(bucket.get(j).value);
            }
        }
        return allValues;
    }
    
    /**
     * Calculate bucket index from key's hash code.
     */
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * Rehash when load factor exceeded - double capacity.
     * Time: O(n)
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int oldCapacity = capacity;
        capacity *= 2;
        DynamicList<Entry<K, V>>[] oldBuckets = buckets;
        
        buckets = new DynamicList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new DynamicList<>();
        }
        size = 0;
        
        // Re-insert all entries
        for (int i = 0; i < oldCapacity; i++) {
            DynamicList<Entry<K, V>> bucket = oldBuckets[i];
            for (int j = 0; j < bucket.size(); j++) {
                Entry<K, V> entry = bucket.get(j);
                put(entry.key, entry.value);
            }
        }
    }
    
    /**
     * Internal Entry class for key-value pairs.
     */
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

