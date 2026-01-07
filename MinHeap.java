/**
 * A min-heap priority queue that orders flights by price (cheapest on top).
 * Uses an array representation of a binary heap.
 * 
 * Source: AI-generated based on standard binary min-heap with array representation
 */
public class MinHeap {
    private DynamicList<Flight> heap;
    
    public MinHeap() {
        this.heap = new DynamicList<>();
    }
    
    /**
     * Add a flight to the heap and move it up if needed.
     * Time: O(log n)
     */
    public void insert(Flight flight) {
        heap.add(flight);
        heapifyUp(heap.size() - 1);
    }
    
    /**
     * Look at the cheapest flight without removing it.
     * Time: O(1)
     */
    public Flight peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }
    
    /**
     * Take out the cheapest flight (and reorganize the heap).
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
     * Turn a list of flights into a heap (could be optimized but this works).
     * Time: O(n log n)
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
     * Move an element up the heap if it's smaller than its parent.
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
     * Move an element down the heap, swapping with smaller children as needed.
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

