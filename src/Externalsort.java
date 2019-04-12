import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 1.0
 */
public class Externalsort 
{
	// On my honor: 
	//
	// - I have not used source code obtained from another student,
	// or any other unauthorized source, either modified or
	// unmodified. 
	//
	// - All source code and documentation used in my program is
	// either my original work, or was derived by me from the
	// source code published in the textbook for this course. 
	//
	// - I have not discussed coding details about this project with
	// anyone other than my partner (in the case of a joint
	// submission), instructor, ACM/UPE tutors or the TAs assigned
	// to this course. I understand that I may discuss the concepts
	// of this program with other students, and that another student
	// may help me debug my program so long as neither of us writes
	// anything during the discussion or modifies any computer file
	// during the discussion. I have violated neither the spirit nor
	// letter of this restriction.
	
	/**
	 * 
	 */
	private static RandomAccessFile runFile;
	
	/**
	 * 
	 */
	private static MergeSort mergeObject;
	
	/**
	 * 
	 */
	private static byte[] INPUTBUFFER;
	
	/**
	 * 
	 */
	private static byte[] OUTPUTBUFFER;
	
	/**
	 * 
	 */
	private static List<byte[]> inputBuffers = new ArrayList<byte[]>();
	
	/**
	 * 
	 */
	private static long BLOCK_OFFSET = 8192;
	
	private static int NUM_BYTES_PER_RECORD = 16;
	
	private static int NUM_RECORDS = 512;
	
	private static Pair<Long, Boolean>[] pointerList;
	
	private static long endOfFile;
	
	/**
	 * Default Constructor
	 */
	public Externalsort() 
	{
		// TODO Auto-generated constructor stub
	}
    
	/**
	 * 
	 * @param args should include the filename
	 */
	public static void main(String[] args) 
	{
	    BinParse p = new BinParse();
	    // create merge sort object
        mergeObject = p.parse(args[0]);
        // set run file object
        runFile = p.getRunFile();
        
        try
        {
            runFile = new RandomAccessFile(new File(p.getRunFileName()), "rw");        	
        }
		catch (IOException err)
		{
			System.out.println("IOException exception: " + err.getMessage());
		}
        
        INPUTBUFFER = p.getInput();
        OUTPUTBUFFER = p.getOutput();
        pointerList = new Pair[p.getPointerList().size()];
        fillPointerList(p.getPointerList());
        
        endOfFile = p.getEndFilePtr();
        
        // call function that begins merge
        beginMerge();
        
	}
	
	/**
	 * 
	 * @param oldPointerList
	 */
	private static void fillPointerList(List<Long> oldPointerList)
	{
		for (int i = 0; i < pointerList.length; i++)
		{
			Pair<Long, Boolean> p = new 
					Pair<Long, Boolean>(oldPointerList.get(i), true);
			pointerList[i] = p;
		}
	}
	
	/**
	 * 
	 * @param pointerList should be the list of file pointers separating runs
	 * @return total number of blocks as an integer
	 */
	private int calculateTotalBlocks(List<Long> ptrList)
	{
		int totalBlocks = 0;
		for (int i = 0; i < ptrList.size() - 1; i++)
		{
			// Get number of blocks for the current run
			totalBlocks += (ptrList.get(i + 1) - ptrList.get(i)) 
					/ BLOCK_OFFSET;
			// Check to see if the run has a partial block as remainder
			if ((ptrList.get(i + 1) - ptrList.get(i)) % BLOCK_OFFSET 
					!= 0)
			{
				totalBlocks++;
			}
		}
		return totalBlocks;
	}
	
	/**
	 * 
	 * @param blockIndex
	 * @param runIndex
	 */
	private static Pair<Integer, Integer> createInputBuffers(int blockIndex, int runIndex)
	{
		int i = runIndex;
		while (i < pointerList.length) // && (i % 8 == 0 && i > 7))
		// for (int i = 0; i < pointerList.length; i++)
		{
			byte[] newIB = new byte[(int)BLOCK_OFFSET];
			byte[] partialIB;
			
			// Check to see if the blcok is full sized or partial
			long blockSize = checkSize(runIndex, blockIndex);
			
			try
			{
				// Case 1) 0 < blockOffset < 8192
				if (blockSize > 0 && blockSize <= BLOCK_OFFSET)  
				{
					partialIB = new byte[(int)blockSize];
					if (runFile.read(partialIB) != -1)
					{
						inputBuffers.add(partialIB);
					}
				
					// This is the end of this run, so set to false
					pointerList[i].setValue(false);
				}
				// Case 2) blockSize > 8192
				else if (blockSize > BLOCK_OFFSET)
				{
					if (runFile.read(newIB) != -1)
					{
						inputBuffers.add(newIB);
					}
				}
			}
			catch (IOException err)
			{
				System.out.println("IOException exception: " + err.getMessage());
			}
            i++;
		}
		
		return new Pair<Integer, Integer>(blockIndex, i);
	}
	
	/**
	 * 
	 * @param runIndex current run 
	 * @param blockIndex current block within current run
	 * @return how far away current block start is from end of file
	 */
	private static long checkSize(int runIndex, int blockIndex)
	{
		return (endOfFile - (pointerList[runIndex].getKey() + 
		                     blockIndex * BLOCK_OFFSET));
	}
	
	/**
	 * 
	 * Begin merge sorting the runs
	 */
	private static void beginMerge()
	{
		// The current block across all runs
		int currBlock = 0;
		int currRunIndex = 0;
		
		while (!allRunsAtAnEnd())
		{
			// Set the run index back to the start run for every iteration
			//currRunIndex = 0;
			
			// Case where the total number of runs is less than or equal to 8
			if (pointerList.length <= 8)
			{
				Pair<Integer, Integer> p = createInputBuffers(currBlock, currRunIndex);
				blockMergeInsert();
				
				currBlock = p.getKey();
				currRunIndex = 0;
			}
			else
			{
				while (currRunIndex < pointerList.length)
				{
					createInputBuffers(currBlock, currRunIndex);
					blockMergeInsert();
				}
			}
			currBlock++;
		}
	}
	
	/**
	 * Check to see if all runs have finished.
	 * @return boolean value dependent on if all runs have hit their end
	 */
	private static boolean allRunsAtAnEnd()
	{
		for (int i = 0; i < pointerList.length; i++)
		{
			if (pointerList[i].getValue() == true)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * block of bytes to be inserted into merge array
	 */
	private static void blockMergeInsert()
	{
		// indicates what entry was removed from record array
		int removed = -1;
		// records list to hold top records
		ArrayList<Record> recordList = new ArrayList<Record>(inputBuffers.size());
		// list of indexes to keep track of where you are in the block
		ArrayList<Integer> indexList = new ArrayList<Integer>(inputBuffers.size());
		// initialize to 0
		
		for (int i = 0; i < inputBuffers.size(); i++)
		{
			indexList.add(i, 0);
		}
		
		for (int j = 0; j < NUM_RECORDS; j++)
		{
			// case where array is empty and top of all blocks are needed
			if (j == 0)
			{
				// fill all of record list with top records
				for (int e = 0; e < inputBuffers.size(); e++)
				{
					byte[] id = Arrays.copyOfRange(inputBuffers.get(e), 
							0 * NUM_BYTES_PER_RECORD, 
							(0 * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2));
				    byte[] key = Arrays.copyOfRange(inputBuffers.get(e), 
				    		(0 * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2), 
				    		(0 * NUM_BYTES_PER_RECORD) + NUM_BYTES_PER_RECORD);
				    Record insertThis = bytesToRecord(id, key);
				    recordList.add(e, insertThis);
				}
			}
			else
			{    
				// get the record from the block that got removed
				if ((indexList.get(removed) + 1) < 512)
				{
					// update the input buffer changed to the next record
					indexList.set(removed, (indexList.get(removed)+1));
					// get the bytes from the index in the 
					byte[] id = Arrays.copyOfRange(inputBuffers.get(removed), 
							indexList.get(removed) * NUM_BYTES_PER_RECORD, 
							(indexList.get(removed) * NUM_BYTES_PER_RECORD) + 
							(NUM_BYTES_PER_RECORD / 2));
				    byte[] key = Arrays.copyOfRange(inputBuffers.get(removed), 
				    		(indexList.get(removed) * NUM_BYTES_PER_RECORD) + 
				    		(NUM_BYTES_PER_RECORD / 2), 
				    		(indexList.get(removed) * NUM_BYTES_PER_RECORD) + 
				    		NUM_BYTES_PER_RECORD);
				    Record insertThis = bytesToRecord(id, key);
				    recordList.set(removed, insertThis);
				}
			}
			
			// removes the smallest record and returns of location of
			// empty entry
			removed = removedSmallestRecord(recordList);
		}
	}
	
	private static int removedSmallestRecord(List<Record> currentList)
	{
		int result = 0;
		Record smallest = currentList.get(0);
		// start at one because biggest is already 0 as set above
		for (int i = 1; i < currentList.size(); i++)
		{
			Record temp = currentList.get(i);
			if (smallest.getKey() > temp.getKey() )
			{
				smallest = temp;
				result = i;
			}
		}
		
		// insert smallest into the merge list
		if (mergeObject.getMergeSize() >= mergeObject.getMaxSize())
		{
			// removes 512 smallest records and adds them to output
			writeToOutput();
		}
        // add to the array regardless of size
		mergeObject.mergeInsert(smallest);
		
		return result;
	}
	
	/**
	 * 
	 * @param id byte array 
	 * @param key byte array
	 * @return Record created from the byte arrays passed in
	 */
	private static Record bytesToRecord(byte[] id, byte[] key)
	{
		// Convert the key and id buffers to double and long 
		long rid = ByteBuffer.wrap(id).getLong();
		double rkey = ByteBuffer.wrap(key).getDouble();
		
		return new Record(rid, rkey);
	}
	
	/**
	 * 
	 */
	private static void writeToOutput()
	{
		for (int i = 0; i < NUM_RECORDS; i++)
		{
			 Record outputRecord = mergeObject.removeSmallest();
		}
	}
	
}
