package org.example;

import java.util.LinkedList;
import java.util.Iterator;

public class MyHashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int capacity;
    private int size = 0;
    private LinkedList<Entry<K, V>>[] buckets;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity) {
        this.capacity = capacity;
        buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
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
        if (key == null) {
            return null;
        }
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        Iterator<Entry<K, V>> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.key.equals(key)) {
                iterator.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }
        for (int i = 0; i < capacity; i++) {
            for (Entry<K, V> entry : buckets[i]) {
                int newIndex = Math.abs(entry.key.hashCode() % newCapacity);
                newBuckets[newIndex].add(entry);
            }
        }
        this.buckets = newBuckets;
        this.capacity = newCapacity;
    }

    public void printAll() {
        System.out.println("MyHashMap (size: " + size + ", capacity: " + capacity + "):");
        for (int i = 0; i < capacity; i++) {
            if (!buckets[i].isEmpty()) {
                System.out.print("Bucket " + i + ": ");
                for (Entry<K, V> entry : buckets[i]) {
                    System.out.print("[" + entry.key + "=" + entry.value + "] ");
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("Один", 1);
        map.put("Два", 2);
        map.put("Три", 3);
        map.printAll();
        System.out.println("get('Один'): " + map.get("Один"));
        System.out.println("get('Четыре'): " + map.get("Четыре"));
        map.put("Два", 22);
        System.out.println("После обновления 'Два':");
        map.printAll();
        System.out.println("containsKey('Три'): " + map.containsKey("Три"));
        System.out.println("size(): " + map.size());
        System.out.println("remove('Один'): " + map.remove("Один"));
        map.printAll();
        map.clear();
        System.out.println("После clear - isEmpty: " + map.isEmpty());
        MyHashMap<Integer, String> resizeMap = new MyHashMap<>(2);
        for (int i = 0; i < 5; i++) {
            resizeMap.put(i, "Value" + i);
        }
        resizeMap.printAll();
    }
}
