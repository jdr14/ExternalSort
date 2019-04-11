
/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 1.2.3
 *
 */
public class MergeSort {
	
	/**
	 * max size of the array 
	 */
	private int maxSize = 512;
	
	/**
	 * current size of merge array
	 */
	private int mergeSize;
	
    /**
     * array of records used for performance
     */
	private Record[] recordArray;
	
	/**
	 * Temporary default constructor
	 * @param heAp to gain access to array
	 */
	public MergeSort(MinHeap heAp) 
	{
		recordArray = heAp.getArray();   
		mergeSize = 0;
	}
	
	/**
	 * 
	 * @param newEntry
	 */
	public void mergeInsert(Record newEntry)
	{
		// case where merge array is empty
		if (mergeSize == 0)
		{    // add to beginning of array
			recordArray[mergeSize] = newEntry;
		}
		else   // else figure out where the record goes
		{
			// take the largest entry currently in the array
			Record maxEntry = recordArray[mergeSize-1];
			
			// if the max entry is smaller than new entry
			// add new entry to end of array
			if(newEntry.getKey() >= maxEntry.getKey())
			{
				recordArray[mergeSize] = newEntry;
			}
			else // place in correct location
			{
				// return index of where to insert
				int insertHere = findCorrectIndex(newEntry);
				
				// move other variables down
				shiftDown(insertHere);
				
				// add new entry into the correct location
				recordArray[insertHere] = newEntry;
			}
		}
		mergeSize++;
	}
	
	/**
	 * 
	 * @return the record that was removed
	 */
	public Record removeSmallest()
	{
		Record result = recordArray[0];
		// remove record from array
		shiftUp();
		// change merge size to reflect change
		mergeSize--;
		
		return result;
	}
	
	/**
	 * Shifts all of the entries in an array up one
	 */
	private void shiftUp()
	{
		// for the length of the array, move entries up one
		for (int i = 1; i < mergeSize; i++)
		{
			recordArray[i-1] = recordArray[i];
		}
	}
	
	/**
	 * 
	 * @return true if the merge array is full
	 */
	public boolean isMergeFull()
	{
		return mergeSize >= maxSize;
	}
	
    /**
     * 
     * @param reCord
     * @return the correct index where new record belongs
     */
	private int findCorrectIndex(Record reCord)
	{
		// iterate through current array
		for(int i = 0; i < mergeSize; i++)
		{
			// create a temporary record variable at location in array
			Record temp = recordArray[i];
			
			// if temporary variable is greater than, found index
			if (temp.getKey() > reCord.getKey())
			{
				return i;
			}
		}
		return mergeSize;
	}
	
	/**
	 * 
	 * @param startIndex
	 */
	private void shiftDown(int endIndex)
	{
		// save largest entry so its not lost
		Record holdRecord = recordArray[mergeSize];
		for (int i = mergeSize; i > endIndex; i--)
		{
	        recordArray[i] = recordArray[i-1];	
		}
		// if there is space in the array, add the thing at end back
		if ((mergeSize + 1) < maxSize)
		{
			recordArray[mergeSize+1] = holdRecord;
		}
	}
	
	/**
	 * 
	 * @return merge array size
	 */
	public int getMergeSize()
	{
		return mergeSize;
	}
	
	/**
	 * 
	 * @return max size of array
	 */
	public int getMaxSize()
	{
		return maxSize;
	}
}
