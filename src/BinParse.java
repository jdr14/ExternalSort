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
	public List<Byte> ParseAsBytes(String fileName)
	{
		List<Byte> lb = new ArrayList<Byte>();
		
		try
		{
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			
			byte[] byteArray = new byte[8192];
			raf.read(byteArray, 0, 8192);
			
			// create a record class that sorts the bits
			Record[] recordArray = new Record[512];
			
			for (int i = 0; i < recordArray.length; i++) 
			{    
				// create a minheap class. cant use any of the functions for heap
				recordArray[i] = //ith 16 bytes in byteArray 
			}
		}    // minheap size is 8 blocks so read in 8 blocks, put each in minheap and then fill output buffer (size one block)
        catch (FileNotFoundException e)
		{    // minheap should by default store by records by size
        	System.err.println("File not found: " + e);
		}
		catch (IOException e)
		{
			System.err.println("Writing error: " + e);
		}
		
		return lb;
	}
}
