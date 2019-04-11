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
	private static void createInputBuffers(int blockIndex, int runIndex)
	{
		int i = runIndex;
		while (i < pointerList.length && i != 8)
		// for (int i = 0; i < pointerList.length; i++)
		{
			byte[] newIB = new byte[(int)BLOCK_OFFSET];
			byte[] partialIB;
			
			long blockSize = checkSize(blockIndex);
			
			try
			{
				if (blockSize > 0 && blockSize <= BLOCK_OFFSET)  // 0 < blockOffset < 8192
				{
					partialIB = new byte[(int)blockSize];
					runFile.read(partialIB);
					inputBuffers.add(partialIB);
				}
				else if (blockSize > BLOCK_OFFSET)
				{
					runFile.read(newIB);
					inputBuffers.add(newIB);
				}
			}
			catch (IOException err)
			{
				System.out.println("IOException exception: " + err.getMessage());
			}
            i++;
		}
		runIndex += i;  // Make sure run index is updated
	}
	
	private static long checkSize(int index)
	{
		return (endOfFile - pointerList[index].getKey());
	}
	
	/**
	 * 
	 * @param pointerList
	 */
	private static void beginMerge()
	{
		// The current block across all runs
		int currBlock = 0;
		int currRunIndex = 0;
		
		// Case where the total number of runs is less than or equal to 8
		if (pointerList.length <= 8)
		{
			createInputBuffers(currBlock, currRunIndex);
			
		}
		// Case where total number of runs is greater than 8
		else
		{
			while (currRunIndex < pointerList.length)
			{
				createInputBuffers(currBlock, currRunIndex);
				//sort(InputBuffers);
			}
		}
	}
	
	/**
	 * 
	 * @param block of bytes to be inserted into merge array
	 */
	private static void blockMergeInsert()
	{
		for (int i = 0; i < inputBuffers.size(); i++)
		{
			byte[] tempInputBuffer = inputBuffers.get(i);
			
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
