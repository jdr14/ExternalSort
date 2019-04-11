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
	 * @param pointerList
	 */
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
							mergeObject.mergeInsert(insertThis);
							
						}
						
						// update pointer to file location
						pointerList.set(i, runFile.getFilePointer());
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
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error in beginMerge: " + e.getMessage());
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
}
