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
	private static long BLOCK_OFFSET;
	
	private static int NUM_BYTES_PER_RECORD = 16;
	
	private static int NUM_RECORDS = 512;
	
	/**
	 * Default Constructor
	 */
	public Externalsort() 
	{
		// TODO Auto-generated constructor stub
		BLOCK_OFFSET = 8192;
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
        INPUTBUFFER = p.getInput();
        OUTPUTBUFFER = p.getOutput();
        // call function that begins merge
        beginMerge(p.getPointerList());
	}
	
	/**
	 * 
	 * @param pointerList should be the list of file pointers separating runs
	 * @return total number of blocks as an integer
	 */
	private int calculateTotalBlocks(List<Long> pointerList)
	{
		int totalBlocks = 0;
		for (int i = 0; i < pointerList.size() - 1; i++)
		{
			// Get number of blocks for the current run
			totalBlocks += (pointerList.get(i + 1) - pointerList.get(i)) 
					/ BLOCK_OFFSET;
			// Check to see if the run has a partial block as remainder
			if ((pointerList.get(i + 1) - pointerList.get(i)) % BLOCK_OFFSET 
					!= 0)
			{
				totalBlocks++;
			}
		}
		return totalBlocks;
	}
	
	/**
	 * 
	 * @param pointerList
	 */
	private static void beginMerge(List<Long> pointerList)
	{
		try
		{
			int beginCurrRun = 0;
			int beginCurrBlock = 0;
			int loopCount = 0;
			
			long beginRunPointer;
			long endRunPointer;
			long lengthCurrRun;
			long numBlocksInCurrRun;
			
			// Iterate through all of the runs
			while (beginCurrRun < pointerList.size() - 1)
			{
				// Get the beginning and end pointers of the current run
				beginRunPointer = pointerList.get(beginCurrRun);
				endRunPointer = pointerList.get(beginCurrRun + 1);
				
				// Length of the current run = end - beginning
				lengthCurrRun = (endRunPointer - beginRunPointer);
				
				// Number of blocks in current run is the length (in bytes)
				// of the current run divided by the block size (in bytes)
				numBlocksInCurrRun = (lengthCurrRun / BLOCK_OFFSET);  
				
				// Account for getting the end of block by subtracting 1
				if (beginCurrBlock < numBlocksInCurrRun - 1)
				{
					// Set the starting point before reading input
					runFile.seek(beginRunPointer + beginCurrBlock);
					
					// Read the next block into a temporary byte array 
					if (runFile.read(INPUTBUFFER) == -1)
					{
						return;
					}
					
					// TODO: remove the comment below once the function exists!!!!
					// blockMergeInsert();
				}
				else if (beginCurrBlock == numBlocksInCurrRun - 1)
				{
					// Check to see if there is a partial block
					if (lengthCurrRun % BLOCK_OFFSET != 0)
					{
						// Number of bytes (less than the block offset of 8192
						long remainder = (lengthCurrRun % BLOCK_OFFSET);
						
						byte[] temp = new byte[(int)remainder];
						
						// Read the partial block into a partial block byte array
						runFile.read(temp);
						
						// TODO: remove the comment below once the function exists!!!!
						// blockMergeInsert(remainder);

					}
					else  // If no remainder, parse as one more normal block
					{
						// Set the starting point before reading input
						runFile.seek(beginRunPointer + beginCurrBlock);
						
						// TODO: remove the comment below once the function exists!!!!
						// blockMergeInsert();
					}
				}
				else
				{
					// run is finished
					continue;
				}
				
				if (beginCurrBlock == 7)
				{
					// Track # of (sets of 8 blocks) we have merge sorted
					loopCount++;
					break;
				}
				
				beginCurrBlock++;
			}  // end while
		}  // end try
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.err.println("Error in beginMerge: " + e.getMessage());
		}
	}

	/*
	private static void beginMerge(List<Long> pointerList)
	{
		try 
		{
			int i = 0;
			
			while (i < pointerList.size())
//			for (int i = 0; i < pointerList.size(); i++)
			{
				long currentPointer = pointerList.get(i);
				
				if ((i + 1) != pointerList.size())
				{
					if ((currentPointer + BLOCK_OFFSET) < pointerList.get(i+1))
					{
						runFile.seek(pointerList.get(i));
						if (runFile.read(INPUTBUFFER) == -1)
						{
							return;
						}
						for (int e = 0; e < NUM_RECORDS; e++) 
						{    
							byte[] id = Arrays.copyOfRange(INPUTBUFFER, 
									e * NUM_BYTES_PER_RECORD, 
									(e * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2));
							byte[] key = Arrays.copyOfRange(INPUTBUFFER, 
									(e * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2), 
									(e * NUM_BYTES_PER_RECORD) + NUM_BYTES_PER_RECORD);
							
							// Record to be inserted into merge array
							Record insertThis = bytesToRecord(id, key);
							// if at max capacity of merge array, write to output buffer/ file
							if (mergeObject.getMergeSize() >= mergeObject.getMaxSize())
							{
								// removes 512 smallest records and adds them to output
								writeToOutput();
							}
                            // add to the array regardless of size
							mergeObject.mergeInsert(insertThis);
							
						}
						
						// update pointer to file location
						pointerList.set(i, runFile.getFilePointer());
						i++;
//						if (i)
					}
					else
					{
						i++;
					}
				}
				else
					break;
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.err.println("Error in beginMerge: " + e.getMessage());
		}
	}
	*/
	
	/**
	 * 
	 * @param id
	 * @param key
	 * @return
	 */
	private static Record bytesToRecord(byte[] id, byte[] key)
	{
		// Convert the key and id buffers to double and long 
		long rid = ByteBuffer.wrap(id).getLong();
		//System.out.println(recordID);
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
//			 Record outputRecord = mergeObject.removeSmallest();
		}
	}
	
}
