import java.io.*;
import java.util.*;
/**
 * Class to parse the binary input file
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 1.0
 */
public class BinParse 
{
	/**
	 * defines the number of records in a block
	 */
	private int NUM_RECORDS = 512;
	
	/**
	 * defines the number of bytes in a record
	 */
	private int NUM_BYTES_PER_RECORD = 16;
	
	/**
	 * to determine the length of the block
	 */
	private int BLOCK_OFFSET = NUM_RECORDS * NUM_BYTES_PER_RECORD;
	
	/**
	 * 
	 * @param fileName to name the binary file that needs to be read in
	 * @return an array list of bytes.
	 */
	public MinHeap ParseAsBytes(String fileName)
	{
		// create a record class that sorts the bits
		Record[] recordArray = new Record[NUM_RECORDS];
		MinHeap newHeap = new MinHeap();
		
		try
		{
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			
			byte[] byteArray = new byte[BLOCK_OFFSET];
			raf.read(byteArray, 0, BLOCK_OFFSET);
			
			for (int i = 0; i < recordArray.length; i++) 
			{    
				// create a minheap class. cant use any of the functions for heap
				byte[] id = Arrays.copyOfRange(byteArray, 
						i * NUM_BYTES_PER_RECORD, 
						(i * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2));
				byte[] key = Arrays.copyOfRange(byteArray, 
						(i * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2), 
						(i * NUM_BYTES_PER_RECORD) + NUM_BYTES_PER_RECORD);
				/*recordArray[i] =*/ newHeap.insert(new Record(id, key));  //ith 16 bytes in byteArray 
			}
			raf.close();
		}    // minheap size is 8 blocks so read in 8 blocks, put each in minheap and then fill output buffer (size one block)
        catch (FileNotFoundException e)
		{    // minheap should by default store by records by size
        	System.err.println("File not found: " + e);
		}
		catch (IOException e)
		{
			System.err.println("Writing error: " + e);
		}
		
		return newHeap;
	}
}
