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
			
			for (int i = 0; i < BLOCK_OFFSET; i++)
			{
				int currOffset = i * 8;
				Byte currByte = raf.readByte();
				lb.add(currByte);
				raf.seek(currOffset);
			}
		}
        catch (FileNotFoundException e)
		{
        	System.err.println("File not found: " + e);
		}
		catch (IOException e)
		
		{
			System.err.println("Writing error: " + e);
		}
		
		return lb;
	}
}
