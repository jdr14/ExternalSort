/**
 * 
 * @author Jovany Cabrera jovanyc4
 * @author Joey Destin Rodgers jdr14
 * @version 1.1.1
 *
 */
public class MinHeap {
	/**
	 * maximum size of min heap
	 */
	private int maxSize = 4096;
	
	/**
	 * create the array called heap because our heap is based off of an
	 * array based implementation
	 */
	private Record[] heap;
	
	/**
	 * Current number of items stored in the array
	 * This should be greater than or equal to the number of items
	 * actually stored in the heap
	 */
	private int arraySize;
	
	/**
	 * Keeps track of the number of items stored in the heap
	 */
	private int heapSize;
	
	/**
	 * Keeps track of the number of items stored in the array, but outside 
	 * the heap.
	 */
	private int numItemsOutsideHeap;
	
	/**
	 * Default Constructor
	 */
	public MinHeap()
	{
		heap = new Record[maxSize];
		arraySize = 0;
		heapSize = 0;
		numItemsOutsideHeap = 0;
	}
	
	/**
	 * Overloaded constructor
	 * @param r
	 * @param size
	 */
	public MinHeap(Record[] r, int nItems, int hSize)
	{
		heap = r;
		arraySize = nItems;
		heapSize = hSize;
		numItemsOutsideHeap = arraySize - heapSize;
		minHeapify();
	}
	
	/**
	 * 
	 * @return HEAP_SIZE an integer which keeps track of how many items are
	 * currently stored in the min heap
	 */
	public int getHeapSize()
	{
		return heapSize;
	}
	
	/**
	 * Return the number of items stored in the array
	 * @return number of items currently stored in the array
	 */
	public int getArraySize()
	{
		return arraySize;
	}
	
	/**
	 * Return the number of records stored outside the heap.
	 * @return number of records stored outside the heap as an int
	 */
	public int getNumItemsOutsideHeap()
	{
		return numItemsOutsideHeap;
	}
	
	/**
	 * Checks if the min heap is full or not
	 * @return true if the min heap is full
	 */
	public boolean heapIsFull()
	{
		return (maxSize - numItemsOutsideHeap - 1 == heapSize);
	}
	
	/**
	 * Compare the number of items to the max size
	 * @return true if the array is filled
	 */
	public boolean arrayIsFull()
	{
		return (maxSize - 1 == arraySize);
	}
	
	/**
	 * Get the left child index based on the parent index/position provided
	 * @param parentPos as an integer needed to determine the child index
	 * @return the left child index as an integer based on the 
	 * parent integer provided.
	 */
	public int getleftChildIndex(int parentPos)
	{
		return (parentPos * 2) + 1;
	}
	
	/**
	 * Get the right child index based on the parent index/position provided
	 * @param parentPos as an integer needed to determine the child index
	 * @return the right child index as an integer based on the 
	 * parent integer provided.
	 */
	public int getRightChildIndex(int parentPos)
	{
		return (parentPos * 2) + 2;
	}
	
	/**
	 * Return the parent index regardless of whether left or 
	 * right child index was provided
	 * @param childPos child position in the array provied as an integer
	 * @return return the integer position of the parent
	 */
	public int getParentIndex(int childPos)
	{
		if (childPos % 2 == 0)  // Case right child
		{
			return (childPos / 2) - 1;
		}
		else  // Case left child
		{
			return (childPos / 2);
		}
	}
	
	/**
	 * Method to retrieve the record at the given position
	 * @param pos position as an integer where the record is stored in array
	 * @return the record requested from the given position
	 */
	public Record getRecord(int pos)
	{
		if (pos >= 0 && pos < heapSize)
		{
			return heap[pos];
		}
		return new Record();
	}
	
	/**
	 * 
	 * @param newEntry of type Record
	 */
	public void insert(Record newEntry)
	{
		// Check that the heap array is within its size limits.
		// assert HEAP_SIZE >= (MAX_SIZE - 1) : "Heap is full"
		if (heapSize >= (maxSize - 1))
		{
			System.out.println("Heap is full!");
		    return;
		}
		
		if (arraySize >= (maxSize - 1))
		{
			System.out.println("Array is full in insert!");
		    return;
		}
		heapSize++; 
		arraySize++;
		
		// Insert the record entry at the next available empty slot in the heap
		heap[heapSize - 1] = newEntry;
		
		// Keep the minHeap array ordered as a min heap
		siftUp();
	}
	
	/**
	 * Method to add items to the array, but not the heap
	 * @param newEntry entry to be added to array but not heap
	 */
	public void addToArray(Record newEntry)
	{
		if (arraySize >= (maxSize - 1) || heapSize >= (maxSize -1))
		{
			System.out.println("Array is full in add to array!");
			return;
		}
		// Does not increase heapSize
		arraySize++;
		
		// Should be inserted at heapSize + 1 or 
		// arraySize - numItemsOutsideHeap
		heap[arraySize - numItemsOutsideHeap] = newEntry;
		
		numItemsOutsideHeap++;
	}
	
	/**
	 * 
	 * @return reference to the smallest record in the heap 
	 * (before it was removed)
	 */
	public Record removeSmallest()
	{
		// Check that records actually exist in the heap
		if (heapSize == 0)
		{
			return new Record();
		}
		
		// Save the removed record in a temp var. to be returned at the end
		Record returnRecord = heap[0];
		
		// Overwrite the top (smallest) record with the last 
		heap[0] = heap[heapSize - 1];
		
		// Now that the last record is root, nullify the last record 
		// and decrement the array and heap size to reflect the new 
		// min heap structure
		heap[heapSize - 1] = null;
		heapSize--;
		arraySize--;
		
		// Uphold the structural integrity of the min heap
		siftDown();
		
		// Return the reference to the removed record for the caller
		return returnRecord;
	}

	/**
	 * Switch the position of 2 places in the array
	 * This method will serve as a helper for organizeMinHeap
	 * @param posRec1 position of one record to be switched
	 * @param posRec2 position of second record to be switched with the first
	 */
	private void swap(int posRec1, int posRec2)
	{
		Record temp = heap[posRec1];
		heap[posRec1] = heap[posRec2];
		heap[posRec2] = temp;
	}
	
	/**
	 * Helper method to keep the integrity of the min heap in tact after insert
	 */
	private void siftUp()
	{
		// No need to continue if the heap size is only 1
		// This should also not be called if the heap size is 0, but 
		// include that case as well as a safety measure
		if (heapSize == 1 || heapSize == 0)
		{
			// No further organization needed...
			return;
		}
		
		Record parent;
		Record rightChild;
		Record leftChild;
		int childIndex = heapSize - 1;
		int parentIndex;
		
		while (true)
		{
			// If the childIndex is even, the child is right
			boolean isRight = (childIndex % 2 == 0);
			
			if (isRight)  // Case right child
			{
				// Calculate index of parent accordingly
				parentIndex = (childIndex / 2) - 1;
				
				// Set appropriate temp vars.
				parent = heap[parentIndex];
				rightChild = heap[childIndex];
				
				// Compare and swap if right child's key is less than parent's
				if (rightChild.getKey() < parent.getKey())
				{
					swap(parentIndex, childIndex);
				}
				
				// If the parent Index is not the root, iterate up through the
				// the tree for more comparisons
				if (parentIndex == 0)
				{
					return;
				}
				childIndex = parentIndex;
			}
			else  // Case left child
			{
				// calculate index of parent accordingly
				parentIndex = (childIndex / 2);
				
				// Set appropriate temp vars.
				parent = heap[parentIndex];
				leftChild = heap[childIndex];
				
				// Compare and swap if right child's key is less than parent's
				if (leftChild.getKey() < parent.getKey())
				{
					swap(parentIndex, childIndex);
				}
                
				// If the parent Index is not the root, iterate up through the
				// the tree for more comparisons
				if (parentIndex == 0)
				{
					return;
				}
				childIndex = parentIndex;
			}
		}  // End While
	}  // End siftUp
	
	public void minHeapify()
	{
		/*
		for (int i = 0; i < arraySize; i++)
		{
			System.out.println(heap[i].getKey());
		}
		*/
		for (int i = heapSize/2 - 1; i >= 0; i--)
		{
			Record parent = heap[i];
			Record leftChild = heap[2*i + 1];
			Record rightChild = null;
			
			if (heapSize % 2 != 0)  // odd 
			{
			    rightChild = heap[2*i + 2];
			}
			
			if (rightChild != null)
			{
				double leftKey = leftChild.getKey();
				double rightKey = rightChild.getKey();
				double parentKey = parent.getKey();
				System.out.println("left key = " + leftKey);
				System.out.println("parent key = " + parentKey);
				System.out.println("right key = " + rightKey);
				
				if (leftKey <= rightKey && leftKey < parentKey)
				{
					// Swap left with the parent (by index)
					swap(i, 2*i + 1);
				}
				else if (rightKey < leftKey && rightKey < parentKey)
				{
				    // Swap right with the parent (by index)
					swap(i, 2*i + 2);
				}
			}
			else
			{
				double leftKey = leftChild.getKey();
				double parentKey = parent.getKey();
				System.out.println("left key = " + leftKey);
				System.out.println("parent key = " + parentKey);
				if (leftKey < parentKey)
				{
					// Swap left with the parent (by index)
					swap(i, 2*i + 1);
				}
			}
		}
		/*
		for (int i = 0; i < arraySize; i++)
		{
			System.out.println(heap[i].getKey());
		}
		*/
	}

	/**
	 * Helper method to keep the integrity of the min heap in tact after remove
	 */
	private void siftDown()
	{
		// No need to continue if the heap size is only 1
		// This should also not be called if the heap size is 0, but 
		// include that case as well as a safety measure
		if (heapSize == 1 || heapSize == 0)
		{
			// No further organization needed...
			return;
		}
		
		// Create descriptive variables to set up the loop below
		int leftChildIndex;
		int rightChildIndex;
		Record leftChild;
		Record rightChild;
		Record parent;
		
		// Iterate through the heap and make sure all nodes are where 
		// they should be
		for (int parentIndex = 0; parentIndex < heapSize / 2; parentIndex++)
		{
			leftChildIndex = (2 * parentIndex) + 1;
			rightChildIndex = (2 * parentIndex) + 2;
			leftChild = heap[leftChildIndex];
			rightChild = heap[rightChildIndex];
			parent = heap[parentIndex];
			
			// If the left child is null, there is no more sorting 
			// that can be done
			if (leftChild == null)
			{
				return;
			}
			
			// Case where right child is null, left is not, 
			// and left is less than parent
			if (rightChild == null)
			{
				// Before returning, check that the left child and parent 
				// are ordered correctly
				if (parent.getKey() > leftChild.getKey())
				{
					swap(parentIndex, leftChildIndex);
				}
				return;
			}
			
			// Case right child is less than parent and less than left child
			if (parent.getKey() > rightChild.getKey() &&
					rightChild.getKey() < leftChild.getKey())
			{
				// Swap the right child with the parent
				swap(parentIndex, rightChildIndex);
			}
			// Case left child is less than parent and less than right child
			else if (parent.getKey() > leftChild.getKey() && 
					leftChild.getKey() <= rightChild.getKey())
			{
				// Swap the left child with the parent
				swap(parentIndex, leftChildIndex);
			}
		}  // End for
	}  // End organizeMinHeap
}
