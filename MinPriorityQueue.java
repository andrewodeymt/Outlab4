public class MinPriorityQueue<T extends Comparable<T>> {
    private T[] heap = (T[]) new Comparable[10];
    //store the position of the first empty slot in the heap
    private int lastItem = 0;
    
    MinPriorityQueue() {
    }

    /**
     * Method to insert items into heap
     *
     * @param newItem
     */
    public void insert(T newItem) {
        //double heap size, if too small
        if (++lastItem == heap.length) {
            doubleHeapSize();
        }
        //insert new item at end of heap
        heap[lastItem] = newItem;

        sortUp(lastItem);
    }

    /**
     * Method to remove item from top of heap
     */
    public T remove() {
        //highest priority value, to be returned
        T returnJob = heap[1];

        //move bottom value to top of heap
        heap[1] = heap[lastItem];

        sortDown(1);

        return returnJob;
    }

    /**
     * method to change the key of an object if it currently exists in the heap
     *
     * @param object
     */
    public void decreaseKey(T temp) {
        Vertex object = (Vertex) temp;
        boolean notFound = true;
        for (int i = 1; i <= lastItem && notFound; i++) {
            Vertex current = (Vertex) heap[i];
            if (current.getVertex() == object.getVertex()) {
                notFound = false;
                current.setDistTo(object.getDistanceTo());
                sortUp(i);
            }
        }
        if (notFound) {
            insert(temp);
        }
    }

    /**
     * Method to return top of heap without removing it
     */
    public T peek() {
        return heap[1];
    }

    /**
     * Method to return whether heap is empty
     */
    public boolean isEmpty() {
        return heap[1] == null;
    }

    /**
     * method to sort the priority queue going up starting at any index
     *
     * @param itemIndex
     */
    private void sortUp(int itemIndex) {
        boolean sorted = false;

        //loop to swim new_item up the heap until sorted
        while (!sorted) {
            //if item is at top, heap is sorted
            if (itemIndex == 1) {
                sorted = true;
            }
            //otherwise sort by priority
            else {
                //if newItem has higher priority than its parent, swim up heap
                if (compareItems(itemIndex, itemIndex / 2) <= 0) {
                    itemIndex = swimUp(itemIndex);
                }
                //otherwise heap is sorted
                else {
                    sorted = true;
                }
            }
        }
    }

    /**
     * Method to swim item up if the heap is out of order
     *
     * @param childIndex
     * @return
     */
    private int swimUp(int childIndex) {
        T temp = heap[childIndex];
        heap[childIndex] = heap[childIndex / 2];
        heap[childIndex / 2] = temp;
        return childIndex / 2;
    }

    /**
     * method to sort the priority queue going down starting at any index
     *
     * @param currentIndex
     */
    private void sortDown(int currentIndex) {
        //heap size has been changed
        boolean changeSize = false;

        //ensure that every item has either two or zero children
        if (lastItem % 2 == 0) {
            heap[lastItem] = null;
            lastItem--;
            changeSize = true;
        }

        int childIndex;
        boolean sorted = false;
        while (!sorted) {
            //if item has no children, heap is sorted
            if (2 * currentIndex > lastItem) {
                sorted = true;
            }
            //otherwise compare priority
            else {
                //finds greater priority item of two child items
                if (compareItems(2 * currentIndex, 2 * currentIndex + 1) <= 0) {
                    childIndex = 2 * currentIndex;
                } else {
                    childIndex = 2 * currentIndex + 1;
                }

                //if parent item has less priority than its child, sink down
                if (!(compareItems(currentIndex, childIndex) <= 0)) {
                    currentIndex = sinkDown(currentIndex);
                }
                //otherwise heap is sorted
                else {
                    sorted = true;
                }
            }
        }

        //removes last value from heap if that has not been done
        if (!changeSize) {
            heap[lastItem] = null;
            lastItem--;
        }
    }

    /**
     * Method to sink item down if the heap is out of order
     *
     * @param parentIndex
     * @return
     */
    private int sinkDown(int parentIndex) {
        T temp = heap[parentIndex];

        int childIndex = 0;
        //which child has greater priority
        if (compareItems(2 * parentIndex, 2 * parentIndex + 1) <= 0) {
            childIndex = 2 * parentIndex;
        } else {
            childIndex = 2 * parentIndex + 1;
        }

        heap[parentIndex] = heap[childIndex];
        heap[childIndex] = temp;
        return childIndex;
    }

    /**
     * Method to compare the priority of two items
     * Returns 1 if value at index_one is larger than value at index_two
     * Returns 0 if values are equivalent
     * Returns -1 if value at index_one is smaller than value at index_two
     *
     * @param indexOne
     * @param indexTwo
     * @return
     */
    private int compareItems(final int indexOne, final int indexTwo) {
        return heap[indexOne].compareTo(heap[indexTwo]);
    }

    /**
     * method to double the size of the heap
     */
    private void doubleHeapSize() {
        T[] temp = (T[]) new Comparable[2 * heap.length];
        for (int i = 0; i < heap.length; i++) {
            temp[i] = heap[i];
        }
        heap = temp;
    }
}
