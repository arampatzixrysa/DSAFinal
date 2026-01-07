/**
 * A resizable array that grows automatically when it gets full.
 * Basically our own version of ArrayList.
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
     * Add an element to the end.
     * Time: O(1) amortized (O(n) when we need to resize)
     */
    public void add(T element) {
        if (size == capacity) {
            resize();
        }
        array[size++] = element;
    }
    
    /**
     * Get element at a position.
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
     * Change the element at a specific position.
     * Time: O(1)
     */
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        array[index] = element;
    }
    
    /**
     * Remove element at position and shift everything over.
     * Time: O(n) - because we have to move all the elements after it
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        @SuppressWarnings("unchecked")
        T removed = (T) array[index];
        
        // Shift everything left
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null; // Let garbage collector clean it up
        return removed;
    }
    
    /**
     * Remove the first occurrence of an element if it exists.
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
     * Check if the list contains a specific element.
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
     * Double the capacity and copy all elements over.
     * Time: O(n) - have to copy everything
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
