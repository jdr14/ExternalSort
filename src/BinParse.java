import java.io.*;
import java.util.*;
import java.nio.*;
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
	 *  Input buffer needed in project spec
	 */
	private byte[] inputBuffer = new byte[BLOCK_OFFSET];
	
	/**
	 * Output buffer needed in project spec
	 */
	private byte[] outputBuffer = new byte[BLOCK_OFFSET];
	
	/**
	 * Current output buffer size
	 */
	private int OUTPUT_BUFFER_SIZE;
	
	/**
	 * List of index file pointers
	 */
	private List<Long> runFilePointers;
	
	/**
	 * Output stream used to create run file
	 */
	FileOutputStream outFile;
	
	/**
	 * String used as the run file name
	 */
	String runFileName;
	
	/**
	 * Latest record inserted in output buffer
	 */
	Record latestInOB;
	
	/**
	 * Latest Record to be inserted into MinHeap
	 */
	Record insertToMinHeap;
	
	/**
	 * Heap used throughout program
	 */
	MinHeap newHeap;
	
	public BinParse()
	{
		newHeap = new MinHeap();
		try 
		{
			runFileName = "run";
			outFile = new FileOutputStream(runFileName);
			runFilePointers = new ArrayList<Long>();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error creating temporary run file: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param fileName to name the binary file that needs to be read in
	 * @return an array list of bytes.
	 */
	public void parse(String fileName)
	{
		// create a record class that sorts the bits
		Record[] recordArray = new Record[NUM_RECORDS];
		OUTPUT_BUFFER_SIZE = 0;
		
		try
		{
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			
			for (int e = 0; e < 8; e++)
			{
				raf.seek(e * BLOCK_OFFSET);
				
				// Input buffer is filled from byte file
				raf.read(inputBuffer, 0, BLOCK_OFFSET);
				
				for (int i = 0; i < recordArray.length; i++) 
				{    
					byte[] id = Arrays.copyOfRange(inputBuffer, 
							i * NUM_BYTES_PER_RECORD, 
							(i * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2));
					byte[] key = Arrays.copyOfRange(inputBuffer, 
							(i * NUM_BYTES_PER_RECORD) + (NUM_BYTES_PER_RECORD / 2), 
							(i * NUM_BYTES_PER_RECORD) + NUM_BYTES_PER_RECORD);
					
					// Record to be inserted into minHeap
					insertToMinHeap = new Record(id, key);
					
					// If the working memory (min heap) is full, send the smallest
					// record to the output buffer
					if (newHeap.isFull())
					{
						// if outputBuffer is full, write to run file and empty
						if (isOutputFull())
						{
                            dump();
						}
						
						// remove root record from min heap and insert into 
						Record smallest = newHeap.removeSmallest();
						addToOutputBuffer(smallest);
						
					}
					
					// check if newest Record can be added to min heap
					if (validAdditionToOB(insertToMinHeap))
					{
						newHeap.insert(insertToMinHeap);  //ith 16 bytes in byteArray	
					}
					else
					{
						// call function to change min heap
					}
				}
			}
			
			// need to close Random Access File
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
		
		
		int i = 0;
	    while (newHeap.getHeapSize() > 0)
	    {
	    	Record temp = newHeap.removeSmallest(); // inputBuffer.getRecord(i);
	    	System.out.print(i + ":  ");
	    	System.out.print("This is ID: " + temp.getID() + " ");
	    	System.out.println("This is key: " + temp.getKey());
	        i++;
	    }
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void dump() throws IOException
	{
		long runFilePointer;
		outFile.write(outputBuffer);
		OUTPUT_BUFFER_SIZE = 0;
		// if output buffer is empty, the latest in output buffer should be empty
//		latestInOB = insertToMinHeap;
	}
	
	/**
	 * 
	 * @return true if output buffer is full
	 */
	private boolean isOutputFull()
	{
		return OUTPUT_BUFFER_SIZE == BLOCK_OFFSET;
	}
	
	/**
	 * 
	 * @param newRecord of type Record to see if should be added to minHeap
	 * @return true if add to minHeap, else false and reduce size
	 */
	private boolean validAdditionToOB(Record newRecord)
	{
		return newRecord.getKey() > latestInOB.getKey();
	}
	
	/**
	 * 
	 * @param smallest is Record removed from heap
	 * @throws IOException 
	 */
	private void addToOutputBuffer(Record smallest) throws IOException
	{
		// keep latest insert to output buffer record up to date
		latestInOB = smallest;
		
		// creates byte arrays out the Record ID and Record Key
		byte[] tempOut1 = new byte[8];
		byte[] tempOut2 = new byte[8];
		ByteBuffer.wrap(tempOut1).putDouble(smallest.getKey());
		ByteBuffer.wrap(tempOut2).putLong(smallest.getID());
		
		// Combines both arrays to create one final array to add to outputBuffer
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(tempOut2);
		output.write(tempOut1);
		byte[] out = output.toByteArray();
		
		// add to output buffer regardless of size
		for (int i = 0; i < out.length; i++)
		{
			outputBuffer[OUTPUT_BUFFER_SIZE] = out[i];
			OUTPUT_BUFFER_SIZE++;
		}
	}
}
