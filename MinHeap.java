/**
 * Min-heap implementation for priority queue of flights.
 * Flights are ordered by current price (cheapest at root).
 * 
 * Source: AI-generated based on standard binary min-heap with array representation
 */
public class MinHeap {
    private DynamicList<Flight> heap;
    
    public MinHeap() {
        this.heap = new DynamicList<>();
    }
    
    /**
     * Insert a flight into the heap.
     * Time: O(log n)
     */
    public void insert(Flight flight) {
        heap.add(flight);
        heapifyUp(heap.size() - 1);
    }
    
    /**
     * Get the cheapest flight (min element) without removing.
     * Time: O(1)
     */
    public Flight peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }
    
    /**
     * Remove and return the cheapest flight.
     * Time: O(log n)
     */
    public Flight extractMin() {
        if (heap.isEmpty()) {
            return null;
        }
        
        Flight min = heap.get(0);
        Flight last = heap.get(heap.size() - 1);
        heap.remove(heap.size() - 1);
        
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return min;
    }
    
    /**
     * Build heap from list of flights.
     * Time: O(n log n) - could be optimized to O(n) with bottom-up heapify
     */
    public void buildHeap(DynamicList<Flight> flights) {
        heap.clear();
        for (int i = 0; i < flights.size(); i++) {
            insert(flights.get(i));
        }
    }
    
    public int size() {
        return heap.size();
    }
    
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * Restore heap property upward from index.
     * Time: O(log n)
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            
            if (heap.get(index).getCurrentPrice() < heap.get(parentIndex).getCurrentPrice()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
    
    /**
     * Restore heap property downward from index.
     * Time: O(log n)
     */
    private void heapifyDown(int index) {
        int size = heap.size();
        
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;
            
            if (leftChild < size && 
                heap.get(leftChild).getCurrentPrice() < heap.get(smallest).getCurrentPrice()) {
                smallest = leftChild;
            }
            
            if (rightChild < size && 
                heap.get(rightChild).getCurrentPrice() < heap.get(smallest).getCurrentPrice()) {
                smallest = rightChild;
            }
            
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }
    
    /**
     * Swap two elements in the heap.
     */
    private void swap(int i, int j) {
        Flight temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}

