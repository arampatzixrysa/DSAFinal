/**
 * Simple queue implementation using circular array.
 * Provides O(1) enqueue and dequeue operations.
 * 
 * Source: AI-generated standard circular queue implementation
 */
public class SimpleQueue<T> {
    private Object[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    
    public SimpleQueue(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }
    
    public SimpleQueue() {
        this(1000); // Default capacity
    }
    
    /**
     * Add element to the rear of the queue.
     * Time: O(1)
     */
    public void enqueue(T element) {
        if (size == capacity) {
            resize();
        }
        rear = (rear + 1) % capacity;
        array[rear] = element;
        size++;
    }
    
    /**
     * Remove and return element from the front of the queue.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T element = (T) array[front];
        array[front] = null; // Help GC
        front = (front + 1) % capacity;
        size--;
        return element;
    }
    
    /**
     * Get front element without removing.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return (T) array[front];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    /**
     * Double capacity when full.
     * Time: O(n)
     */
    private void resize() {
        int newCapacity = capacity * 2;
        Object[] newArray = new Object[newCapacity];
        
        // Copy elements in order
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(front + i) % capacity];
        }
        
        array = newArray;
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
    }
}
