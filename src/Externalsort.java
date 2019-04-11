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
	private static List<byte[]> inputBuffers;
	
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
        INPUTBUFFER = p.getInput();
        OUTPUTBUFFER = p.getOutput();
        pointerList = new Pair[p.getPointerList().size()];
        fillPointerList(p.getPointerList());
        
        endOfFile = p.getEndFilePtr();
        
        // call function that begins merge
        beginMerge();
        
	}
	
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
	
	private static void createInputBuffers()
	{
		for (int i = 0; i < pointerList.length; i++)
		{
			byte[] newIB = new byte[(int)BLOCK_OFFSET];
			
			int blockSize = nextBlockExists(i);
			if (nextBlockExists)
			{
				
			}
			runFile.read()
			inputBuffers.add(newIB);
		}
	}
	
	private int nextBlockExists(int index)
	{
		
		pointerList.get(index + 1);
	}
	
	/**
	 * 
	 * @param pointerList
	 */
	private static void beginMerge()
	{
		
	}
		
		/*
		try
		{
			int beginCurrRun = 0;
			int beginCurrBlock = 0;
			int runCount = 0;
			
			Pair<Long, Boolean> beginRunPointer;
			Pair<Long, Boolean> endRunPointer;
			long lengthCurrRun;
			long numBlocksInCurrRun;
			
			// Iterate through all of the runs
			while (beginCurrRun < pointerList.length - 1)
			{
				if (pointerList[runCount].getValue())
				{
					// Get the beginning and end pointers of the current run
					beginRunPointer = pointerList[beginCurrRun];
					endRunPointer = pointerList[beginCurrRun + 1];
					
<<<<<<< HEAD
					// Read the next block into a temporary byte array 
					if (runFile.read(INPUTBUFFER) == -1)
					{
						return;
					}
					blockMergeInsert();
				}
				else if (beginCurrBlock == numBlocksInCurrRun - 1)
				{
					// Check to see if there is a partial block
					if (lengthCurrRun % BLOCK_OFFSET != 0)
=======
					// Length of the current run (in bytes) = end - beginning
					lengthCurrRun = (endRunPointer.getKey() - beginRunPointer.getKey());
					
					// Number of blocks in current run is the length (in bytes)
					// of the current run divided by the block size (in bytes)
					numBlocksInCurrRun = (lengthCurrRun / BLOCK_OFFSET);  
					
					// Account for getting the end of block by subtracting 1
					if (beginCurrBlock < numBlocksInCurrRun - 1)
>>>>>>> ca0c2bc26ca93ff43aa25017f9cdb833fe989488
					{
						// Set the starting point before reading input
						runFile.seek(beginRunPointer.getKey() + beginCurrBlock);
						
						// Read the next block into a temporary byte array 
						if (runFile.read(INPUTBUFFER) == -1)
						{
							return;
						}
						
						// TODO: remove the comment below once the function exists!!!!
<<<<<<< HEAD
//						 blockMergeInsert(remainder);
=======
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
>>>>>>> ca0c2bc26ca93ff43aa25017f9cdb833fe989488

						}
						else  // If no remainder, parse as one more normal block
						{
							// Set the starting point before reading input
							runFile.seek(beginRunPointer.getKey() + beginCurrBlock);
							
							// TODO: remove the comment below once the function exists!!!!
							// blockMergeInsert();
						}
					}
					else
					{
<<<<<<< HEAD
						// Set the starting point before reading input
						runFile.seek(beginRunPointer + beginCurrBlock);
						
						blockMergeInsert();
=======
						// run is finished
						continue;
>>>>>>> ca0c2bc26ca93ff43aa25017f9cdb833fe989488
					}
					
					if (beginCurrBlock == 7)
					{
						// Track # of (sets of 8 blocks) we have merge sorted
						runCount++;
						break;
					}
					
					beginCurrBlock++;
				}  // end if
			}  // end while
				
		}  // end try
		catch (IOException e) 
		{
			System.err.println("Error in beginMerge: " + e.getMessage());
		}
	}
    */
		
	/*
	private static void beginMerge(List<Long> pointerList)
	{
		try 
		{
//			for (int i = 0; i < pointerList.size(); i++)
			int i = 0;
			
			while (i < pointerList.size())
			{
				long currentPointer = pointerList.get(i);
				
				if ((i + 1) != pointerList.size())
				{
					// check to not enter different run
					if ((currentPointer + BLOCK_OFFSET) < pointerList.get(i+1))
					{
						runFile.seek(pointerList.get(i));
						if (runFile.read(INPUTBUFFER) == -1)
						{
							return;
						}
						// function to turn input buffer into records
						// and insert to merge array
						blockMergeInsert();
						
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
			System.err.println("Error in beginMerge: " + e.getMessage());
		}
	}
	*/
	
	/**
	 * 
	 * @param block of bytes to be inserted into merge array
	 */
	private static void blockMergeInsert()
	{
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
	}
	
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
			 Record outputRecord = mergeObject.removeSmallest();
		}
	}
	
}
