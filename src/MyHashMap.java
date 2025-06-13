public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private MyLinkedList<Entry<K, V>>[] buckets;
    private int size;

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        buckets = (MyLinkedList<Entry<K, V>>[]) new MyLinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new MyLinkedList<>();
        }
        size = 0;
    }

    public void put(K key, V value) {
        if ((double) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        MyLinkedList<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        int index = getIndex(key);
        MyLinkedList<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean remove(K key) {
        int index = getIndex(key);
        MyLinkedList<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return true;
            }
        }
        return false;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        int newCapacity = buckets.length * 2;

        MyLinkedList<Entry<K, V>>[] newBuckets = (MyLinkedList<Entry<K, V>>[]) new MyLinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new MyLinkedList<>();
        }

        rehash(newBuckets);
    }

    private void rehash(MyLinkedList<Entry<K, V>>[] newBuckets) {
        for (MyLinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = entry.key == null ? 0 : (entry.key.hashCode() & Integer.MAX_VALUE) % newBuckets.length;
                newBuckets[newIndex].add(entry);
            }
        }

        buckets = newBuckets;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}