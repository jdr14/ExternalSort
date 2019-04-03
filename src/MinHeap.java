/**
 * 
 * @author Jovany Cabrera jovanyc4
 * @author Joey Destin Rodgers jdr14
 * @version 1.1.0
 *
 */
public class MinHeap {
	/**
	 * maximum size of min heap
	 */
	private int MAX_SIZE = 4096;
	
	/**
	 * 
	 */
	private Record[] heap;
	
	/**
	 * Current size of heap
	 */
	private int HEAP_SIZE;
	
	/**
	 * Default Constructor
	 */
	public MinHeap()
	{
		heap = new Record[MAX_SIZE];
		HEAP_SIZE = 0;
	}
	
	/**
	 * Overloaded constructor
	 * @param r
	 * @param size
	 */
	public MinHeap(Record[] r, int size)
	{
		heap = r;
		HEAP_SIZE = size;
	}
	
	/**
	 * 
	 * @param newEntry of type Record
	 */
	public boolean insert(Record newEntry)
	{
		// Check that the heap array is within its size limits.
		// assert HEAP_SIZE >= (MAX_SIZE - 1) : "Heap is full"
		if (HEAP_SIZE >= (MAX_SIZE - 1))
		{
			return false;
		}
		// Insert the record entry at the next available empty slot in the heap
		heap[HEAP_SIZE - 1] = newEntry;
		HEAP_SIZE++;
		
		// Keep the minHeap array ordered as a min heap
		siftUp();
		return true;
	}
	
	/**
	 * 
	 * @return reference to the smallest record in the heap 
	 * (before it was removed)
	 */
	public Record removeSmallest()
	{
		// Check that records actually exist in the heap
		if (HEAP_SIZE == 0)
		{
			return new Record();
		}
		
		// Save the removed record in a temp var. to be returned at the end
		Record returnRecord = heap[0];
		
		// Overwrite the top (smallest) record with the last 
		heap[0] = heap[HEAP_SIZE - 1];
		
		// Now that the last record is root, nullify the last record 
		// and decrement the size to reflect the new min heap structure
		heap[HEAP_SIZE - 1] = null;
		HEAP_SIZE--;
		
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
		if (HEAP_SIZE == 1 || HEAP_SIZE == 0)
		{
			// No further organization needed...
			return;
		}
		
		Record parent;
		Record rightChild;
		Record leftChild;
		int childIndex = HEAP_SIZE - 1;
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

	/**
	 * Helper method to keep the integrity of the min heap in tact after remove
	 */
	private void siftDown()
	{
		// No need to continue if the heap size is only 1
		// This should also not be called if the heap size is 0, but 
		// include that case as well as a safety measure
		if (HEAP_SIZE == 1 || HEAP_SIZE == 0)
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
		for (int parentIndex = 0; parentIndex < HEAP_SIZE / 2; parentIndex++)
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
