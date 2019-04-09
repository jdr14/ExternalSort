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
	File outFile;
	
	/**
	 * String used as the run file name
	 */
	String runFileName;
	
	/**
	 * Uses file to give position in file
	 */
	RandomAccessFile runFile;
	
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
	
	private boolean StartOfRun;
	
	public BinParse()
	{
		OUTPUT_BUFFER_SIZE = 0;
		StartOfRun = false;
		newHeap = new MinHeap();
        runFileName = "run";
		outFile = new File(runFileName);
		try {
			runFile = new RandomAccessFile(outFile, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Error with creation of run file: " 
		        + e.getMessage());
		}
		runFilePointers = new ArrayList<Long>();
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
			
			for (int e = 0; e < 50; e++)
			{
				raf.seek(e * BLOCK_OFFSET);
				
				int readResult = 11;
				// Input buffer is filled from byte file
				readResult = raf.read(inputBuffer, 0, BLOCK_OFFSET);
				System.out.println("This is readResult: " + readResult);
				if(readResult == -1)
					break;
				
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
					
					// If the working memory (minHeap) is full, send the smallest
					// record to the output buffer
					if (newHeap.arrayIsFull())
					{
						arrayCheck();
					}
					
					// check if newest Record can be added to minHeap
					if (validAdditionToOB(insertToMinHeap))
					{
						newHeap.insert(insertToMinHeap);  //ith 16 bytes in byteArray	
					}
					else
					{
						// call function to change minHeap
						newHeap.addToArray(insertToMinHeap);
					}
				}
			}
			// need to close Random Access File
			raf.close();
			System.out.println("This is pointerarraysize: " + runFilePointers.size());
		}
        catch (FileNotFoundException err)
		{
        	System.err.println("File not found: " + err.getMessage());
		}
		catch (IOException err)
		{
			return;
		}
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	private void arrayCheck() throws IOException {
		// if true, add root to output buffer
		if (newHeap.heapIsFull())
		{
			// check if output buffer is full before adding to it
			if (isOutputFull())
			{
				// if full write to output file
				dump();
			}
			
			// remove root of heap and add to output buffer
			addToOutputBuffer(newHeap.removeSmallest());
		}
		else if ((newHeap.getHeapSize() == 0) && (newHeap.arrayIsFull()))
		{
			// re-heap the array
			
			// flag that beginning of run
			StartOfRun = true;
		}
	}
	/**
	 * 
	 * @throws IOException
	 */
	private void dump() throws IOException
	{
		// if at beginning of run, get file pointer for merge sort
		if (StartOfRun)
		{
			long runFilePointer = runFile.getFilePointer();
			runFilePointers.add(runFilePointer);
			// switch the flag back to false
			StartOfRun = false;
		}
		
		// write output buffer to run file
		runFile.write(outputBuffer);
		
		// reset output buffer size
		OUTPUT_BUFFER_SIZE = 0;
		// if output buffer is empty, reset latest in output buffer variable
		latestInOB = null;
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
		// case where output buffer is empty
		if (latestInOB == null)
		{
			return true;
		}
		
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
