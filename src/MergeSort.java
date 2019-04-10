import java.util.*;
import java.io.*;

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
	private int maxSize = 4096;
	
	private int mergeSize;
	
    /**
     * array of records used for performance
     */
	private Record[] recordArray;
	
	/**
	 * Temporary default constructor
	 * @param heAp to gain access to array
	 */
	public MergeSort(MinHeap heAp) {
		recordArray = heAp.getArray();
		mergeSize = 0;
	}
	
	/**
	 * 
	 * @param newEntry
	 */
	public void mergeInsert(Record newEntry)
	{
		if (mergeSize == 0)
		{
			recordArray[mergeSize] = newEntry;
		}
		else
		{
			// only add to merge array if index is smaller than max size
			if (mergeSize < maxSize)
			{
				Record maxEntry = recordArray[mergeSize-1];
				System.out.println("This is mergesize: " + mergeSize);
				if(newEntry.getKey() > maxEntry.getKey())
				{
					recordArray[mergeSize] = newEntry;
				}
				else
				{
					// return index of where to insert
					int insertHere = findCorrectIndex(newEntry);
					shiftDown(insertHere);
					recordArray[insertHere] = newEntry;
				}
			}
			else
			{
				
			}
		}
		mergeSize++;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMergeFull()
	{
		return mergeSize >= maxSize;
	}
	
    /**
     * 
     * @param reCord
     * @return
     */
	private int findCorrectIndex(Record reCord)
	{
		for(int i = 1; i < mergeSize; i++)
		{
			Record temp = recordArray[i];
			if (temp.getKey() < reCord.getKey())
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
		Record holdRecord = recordArray[mergeSize];
		for (int i = mergeSize; i > endIndex; i--)
		{
	        recordArray[i-1] = recordArray[i];	
		}
		if ((mergeSize + 1) > maxSize)
		{
			// array is full, output buffer
		}
		else
		{
			mergeSize++;
			recordArray[mergeSize] = holdRecord;
		}
		
	}
}
