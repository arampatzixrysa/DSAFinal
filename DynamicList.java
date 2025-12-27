/**
 * Generic dynamic array (resizable list).
 * Automatically grows when capacity is reached.
 * 
 * Source: AI-generated based on standard ArrayList implementation pattern
 */
public class DynamicList<T> {
    private Object[] array;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 10;
    
    public DynamicList() {
        this.capacity = DEFAULT_CAPACITY;
        this.array = new Object[capacity];
        this.size = 0;
    }
    
    public DynamicList(int initialCapacity) {
        this.capacity = initialCapacity;
        this.array = new Object[capacity];
        this.size = 0;
    }
    
    /**
     * Add element to the end of the list.
     * Time: O(1) amortized (O(n) when resizing)
     */
    public void add(T element) {
        if (size == capacity) {
            resize();
        }
        array[size++] = element;
    }
    
    /**
     * Get element at index.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) array[index];
    }
    
    /**
     * Set element at index.
     * Time: O(1)
     */
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        array[index] = element;
    }
    
    /**
     * Remove element at index.
     * Time: O(n) - needs to shift elements
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        @SuppressWarnings("unchecked")
        T removed = (T) array[index];
        
        // Shift elements left
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null; // Help GC
        return removed;
    }
    
    /**
     * Remove first occurrence of element.
     * Time: O(n)
     */
    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if list contains element.
     * Time: O(n)
     */
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return true;
            }
        }
        return false;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }
    
    /**
     * Double the capacity when full.
     * Time: O(n) - copies all elements
     */
    private void resize() {
        capacity *= 2;
        Object[] newArray = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }
}
