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
	private int numRecords = 512;
	
	/**
	 * defines the number of bytes in a record
	 */
	private int numBytesPerRecord = 16;
	
	/**
	 * to determine the length of the block
	 */
	private int blockOffset = numRecords * numBytesPerRecord;
	
	/**
	 *  Input buffer needed in project spec
	 */
	private byte[] inputBuffer = new byte[blockOffset];
	
	/**
	 * Output buffer needed in project spec
	 */
	private byte[] outputBuffer = new byte[blockOffset];
	
	/**
	 * Current output buffer size
	 */
	private int outputBufferSize;
	
	/**
	 * List of index file pointers
	 */
	private List<Long> runFilePointers;
	
	/**
	 * reference to end of data in file
	 */
	private long endFilePtr;
	
	/**
	 * Output stream used to create run file
	 */
	private File outFile;
	
	/**
	 * String used as the run file name
	 */
	private String runFileName;
	
	/**
	 * Uses file to give position in file
	 */
	private RandomAccessFile runFile;
	
	/**
	 * Latest record inserted in output buffer
	 */
	private Record latestInOB;
	
	/**
	 * Latest Record to be inserted into MinHeap
	 */
	private Record insertToMinHeap;
	
	/**
	 * Heap used throughout program
	 */
	private MinHeap newHeap;
	
	/**
	 * Flag to indicate beginning of run
	 */
	private boolean StartOfRun;
	
	/**
	 * default constructor for Bin Parse class
	 */
	public BinParse()
	{
		outputBufferSize = 0;
		StartOfRun = true;
		newHeap = new MinHeap();
        runFileName = "run";
		outFile = new File(runFileName);
		try 
		{
			runFile = new RandomAccessFile(outFile, "rw");
		} 
		catch (FileNotFoundException e) 
		{
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
	public MergeSort parse(String fileName)
	{
		// create a record class that sorts the bits
		outputBufferSize = 0;
		
		try
		{
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			
			// Iterate through the file while there is content to be read
			while (raf.read(inputBuffer) != -1) 
			{
				// Create records and insert into heap
				for (int i = 0; i < numRecords; i++) 
				{    
					byte[] id = Arrays.copyOfRange(inputBuffer, 
							i * numBytesPerRecord, 
							(i * numBytesPerRecord) + (numBytesPerRecord / 2));
					byte[] key = Arrays.copyOfRange(inputBuffer, 
							(i * numBytesPerRecord) + (numBytesPerRecord / 2), 
							(i * numBytesPerRecord) + numBytesPerRecord);
					
					// Create the next record to be inserted into minHeap
					insertToMinHeap = bytesToRecord(id, key);
					
					// If the working memory (minHeap) is full, send the smallest
					// record to the output buffer
					if (newHeap.arrayIsFull())
					{
						// takes care of heap is full case and
						// data outside of heap is full case
						arrayCheck();
					}
					
					// check if newest Record can be added to minHeap
					if (validAdditionToOB(insertToMinHeap))
					{
						newHeap.insert(insertToMinHeap);
					}
					else
					{
						// call function to change minHeap
						newHeap.addToArray(insertToMinHeap);
					}
				}
			}
			// empty out heap if there is stuff after input ends
			emptyHeap();

			// if array has content, empty out
			if (newHeap.getArraySize() != 0)
			{
				// begin new run
				StartOfRun = true;
				// re-heap the array
				newHeap.minHeapify();
				// empty out remaining data
				emptyHeap();
				// Output buffer should now be partially filled
				dump();
			}
			// end of run file pointer
			endFilePtr = raf.getFilePointer();
			
			// need to close Random Access File
			raf.close();
		}
        catch (FileNotFoundException err)
		{
        	System.err.println("File not found: " + err.getMessage());
		}
		catch (IOException err)
		{
			System.out.println("IOException exception: " + err.getMessage());
		}
		return new MergeSort(newHeap);
	}
	
	/**
	 * @throws IOException 
	 * Function used to remove the rest of heap into
	 * output. Continues while heap has content
	 */
	private void emptyHeap() throws IOException
	{
		while (newHeap.getHeapSize() != 0 )
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
	}
	
	/**
	 * @throws IOException 
	 * Function called in main to determine what the content of
	 * the array is and what actions to take when array is full
	 */
	private void arrayCheck() throws IOException 
	{
		// if true, add root to output buffer
		if (newHeap.heapIsFull() && newHeap.getHeapSize() > 0)
		{  // If heap is 4096 items big
			// check if output buffer is full before adding to it
			if (isOutputFull())
			{
				// if output buffer is full write to output file
				dump();
			}
			
			// remove root of heap and add to output buffer
			addToOutputBuffer(newHeap.removeSmallest());
		}
		else if ((newHeap.getHeapSize() == 0) && (newHeap.arrayIsFull()))
		{
			// re-heap the array
			newHeap.minHeapify();
			
			// flag that beginning of run
			StartOfRun = true;
			
			// check if output buffer is full before adding to it
			if (isOutputFull())
			{
				// if output buffer is full write to output file
				dump();
			}
			
			// remove root of heap and add to output buffer
			addToOutputBuffer(newHeap.removeSmallest());
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 * Empty out the output buffer into the run file
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
		outputBufferSize = 0;
		
		// if output buffer is empty, reset latest in output buffer variable
		latestInOB = null;
	}
	
	/**
	 * 
	 * @return true if output buffer is full
	 */
	private boolean isOutputFull()
	{
		return outputBufferSize == blockOffset;
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
		if (smallest != null)
		{
			// keep latest insert to output buffer record up to date
			latestInOB = smallest;
			
			// creates byte arrays out the Record ID and Record Key
			byte[] tempOut1 = new byte[8];
			byte[] tempOut2 = new byte[8];

			ByteBuffer.wrap(tempOut1).putDouble(smallest.getKey());
			ByteBuffer.wrap(tempOut2).putLong(smallest.getID());
			
			// Combines both arrays to create one 
			// final array to add to outputBuffer
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			output.write(tempOut2);
			output.write(tempOut1);
			byte[] out = output.toByteArray();
			
			// add to output buffer regardless of size
			for (int i = 0; i < out.length; i++)
			{
				outputBuffer[outputBufferSize] = out[i];
				outputBufferSize++;
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param key
	 * @return
	 */
	public Record bytesToRecord(byte[] id, byte[] key)
	{
		// Convert the key and id buffers to double and long 
		long rid = ByteBuffer.wrap(id).getLong();
		//System.out.println(recordID);
		double rkey = ByteBuffer.wrap(key).getDouble();
		
		return new Record(rid, rkey);
	}
	
	/**
	 * 
	 * @return runFilePointer array
	 */
	public List<Long> getPointerList()
	{
		return runFilePointers;
	}
	
	/**
	 * 
	 * @return runFile object
	 */
	public RandomAccessFile getRunFile()
	{
		return runFile;
	}
	
	/**
	 * 
	 * @return runFileName as string
	 */
	public String getRunFileName()
	{
		return runFileName;
	}
    
	/**
	 * 
	 * @return input buffer
	 */
	public byte[] getInput()
	{
		return inputBuffer;
	}
	
	/**
	 * 
	 * @return output buffer
	 */
	public byte[] getOutput()
	{
		return outputBuffer;
	}
	
	/**
	 * @return the end of file pointer as long
	 */
	public long getEndFilePtr()
	{
		return endFilePtr;
	}
}