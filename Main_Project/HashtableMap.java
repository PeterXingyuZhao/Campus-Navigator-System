// == CS400 Fall 2024 File Header Information ==
// Name: Xingyu Zhao
// Email: xzhao468@wisc.edu
// Group: P2.3915
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;


@SuppressWarnings("unchecked")
public class HashtableMap <KeyType, ValueType> implements MapADT <KeyType, ValueType> {
    protected class Pair {

        public KeyType key;
        public ValueType value;
    
        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    
    }

    protected LinkedList<Pair>[] table = null;
    protected int tableCapcity = 0; // hashmap capacity
    protected int numElements = 0; // number of elements in the map

    public HashtableMap (int capacity) {
        if(capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        tableCapcity = capacity;
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        for(int i = 0; i < tableCapcity; i++) { // initialize each list
            table[i] = new LinkedList<Pair>();
        }
    }

    public HashtableMap () {
        tableCapcity = 64;
        table = (LinkedList<Pair>[]) new LinkedList[64];
        for(int i = 0; i < tableCapcity; i++) { // initialize each list
            table[i] = new LinkedList<Pair>();
        }
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if(key == null) {
            throw new NullPointerException("The key cannot be null.");
        }
        if(containsKey(key) == true) {
            throw new IllegalArgumentException("The key already maps to a value");
        }
        int hashValue = Math.abs(key.hashCode()) % tableCapcity;
        Pair pair = new Pair(key, value);
        table[hashValue].add(pair); // add the pair to the corresponding list
        numElements++;
        if((double) numElements * 1.0 / tableCapcity >= 0.8) {
            resizeTable(); // load factor is >= 0.8, need to resize
        }
    }

    private void resizeTable() {
        int newTableCapacity = tableCapcity * 2;
        LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[newTableCapacity];
        for(int i = 0; i < newTableCapacity; i++) {
            newTable[i] = new LinkedList<Pair>();
        }
        for(LinkedList<Pair> bucket: table) { // rehash all the pair to the new table
            for(Pair pair: bucket) {
                int newHashValue = Math.abs(pair.key.hashCode()) % newTableCapacity;
                newTable[newHashValue].add(pair);
            }
        }
        table = newTable;
        tableCapcity = newTableCapacity;
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        int hashValue = Math.abs(key.hashCode()) % tableCapcity;
        for(Pair pair: table[hashValue]) { // enumerate all the elements
            if(pair.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        int hashValue = Math.abs(key.hashCode()) % tableCapcity;
        for(Pair pair: table[hashValue]) { // check whether the corresponding list contains this key
            if(pair.key.equals(key)) {
                return pair.value;
            }
        }
        throw new NoSuchElementException("The key is not stored in the hash table");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        int hashValue = Math.abs(key.hashCode()) % tableCapcity;
        for(Pair pair: table[hashValue]) { // remove the pair from the corresponding list
            if(pair.key.equals(key)) {
                ValueType value = pair.value;
                table[hashValue].remove(pair);
                numElements--;
                return value;
            }
        }
        throw new NoSuchElementException("The key is not stored in the hash table");
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
        for(LinkedList<Pair> bucket: table) {
            for(Pair pair: bucket) {
                bucket.remove(pair);
            }
        }
        numElements = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        return numElements;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return tableCapcity;
    }

    /**
     * Retrieves this collection's keys.
     * @return a list of keys in the underlying array for this collection
     */
    @Override
    public List<KeyType> getKeys() {
        List<KeyType> keyList = new ArrayList<>();
        for(LinkedList<Pair> bucket: table) {
            for(Pair pair: bucket) {
                keyList.add(pair.key);
            }
        }
        return keyList;
    }

    // @Test
    // public void testPutContainsKeysAndGet() {
    //     // Test the functionality of adding and retrieving elements, using put, containsKey and get methods.
    //     HashtableMap<String, Integer> map = new HashtableMap<>();
    //     map.put("one", 1);
    //     map.put("two", 2);
    //     Assertions.assertEquals(map.containsKey("one"), true);
    //     Assertions.assertEquals(map.containsKey("three"), false);
    //     Assertions.assertEquals(map.get("one"), 1);
    //     Assertions.assertEquals(map.get("two"), 2);
    // }

    // @Test
    // public void testRemove() {
    //     // Test the functionality of adding and removing keys, using put, remove and containsKey methods.
    //     HashtableMap<String, Integer> map = new HashtableMap<>();
    //     map.put("one", 1);
    //     map.put("two", 2);
    //     Assertions.assertEquals(map.remove("one"), 1);
    //     Assertions.assertEquals(map.containsKey("one"), false);
    // }

    // @Test
    // public void testClear() {
    //     // Test the functionality of adding keys and clearing the map, using put, clear and containsKey methods.
    //     HashtableMap<String, Integer> map = new HashtableMap<>();
    //     map.put("one", 1);
    //     map.put("two", 2);
    //     map.clear();
    //     Assertions.assertEquals(map.containsKey("one"), false);
    //     Assertions.assertEquals(map.containsKey("two"), false);
    // }

    // @Test
    // public void testGetSize() {
    //     // Test the functionality of adding keys and checking for the size, using put and getSize methods.
    //     HashtableMap<String, Integer> map = new HashtableMap<>();
    //     map.put("one", 1);
    //     map.put("two", 2);
    //     Assertions.assertEquals(map.getSize(), 2);
    // }

    // @Test
    // public void testGetCapacity() {
    //     // Test the functionality of adding keys and checking for the underlying array capacity, using put and getCapacity methods.
    //     HashtableMap<String, Integer> map = new HashtableMap<>(30);
    //     map.put("one", 1);
    //     map.put("two", 2);
    //     Assertions.assertEquals(map.getCapacity(), 30);
    // }
}
