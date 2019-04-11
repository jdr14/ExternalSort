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
